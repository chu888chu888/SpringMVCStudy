/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Area;
import net.shopxx.entity.Invoice;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderItem;
import net.shopxx.entity.Payment;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Product;
import net.shopxx.entity.Refunds;
import net.shopxx.entity.Returns;
import net.shopxx.entity.ReturnsItem;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingItem;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.service.AdminService;
import net.shopxx.service.AreaService;
import net.shopxx.service.DeliveryCorpService;
import net.shopxx.service.MemberService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentMethodService;
import net.shopxx.service.ShippingMethodService;
import net.shopxx.service.ShippingService;
import net.shopxx.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 订单
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;
	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 检查锁定
	 */
	@RequestMapping(value = "/check_lock", method = RequestMethod.POST)
	public @ResponseBody
	Message checkLock(Long id) {
		Order order = orderService.find(id);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return Message.warn("admin.order.locked");
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculate(Long id, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount) {
		Map<String, Object> data = new HashMap<String, Object>();
		Order order = orderService.find(id);
		if (order == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("amount", orderService.calculateAmount(order.getPrice(), order.getFee(), freight, tax, order.getPromotionDiscount(), order.getCouponDiscount(), offsetAmount));
		return data;
	}

	/**
	 * 物流动态
	 */
	@RequestMapping(value = "/transit_step", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> transitStep(Long shippingId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Shipping shipping = shippingService.find(shippingId);
		if (shipping == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(shipping.getDeliveryCorpCode()) || StringUtils.isEmpty(shipping.getTrackingNo())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("transitSteps", shippingService.getTransitSteps(shipping));
		return data;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingPayment.equals(order.getStatus()) && !Order.Status.pendingReview.equals(order.getStatus()))) {
			return ERROR_VIEW;
		}
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("order", order);
		return "/admin/order/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Long id, Long areaId, Long paymentMethodId, Long shippingMethodId, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount, Long rewardPoint, String consignee, String address, String zipCode, String phone, String invoiceTitle, String memo,
			RedirectAttributes redirectAttributes) {
		Area area = areaService.find(areaId);
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);

		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingPayment.equals(order.getStatus()) && !Order.Status.pendingReview.equals(order.getStatus()))) {
			return ERROR_VIEW;
		}
		Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) ? new Invoice(invoiceTitle, null) : null;
		order.setTax(invoice != null ? tax : BigDecimal.ZERO);
		order.setOffsetAmount(offsetAmount);
		order.setRewardPoint(rewardPoint);
		order.setMemo(memo);
		order.setInvoice(invoice);
		order.setPaymentMethod(paymentMethod);
		if (order.getIsDelivery()) {
			order.setFreight(freight);
			order.setConsignee(consignee);
			order.setAddress(address);
			order.setZipCode(zipCode);
			order.setPhone(phone);
			order.setArea(area);
			order.setShippingMethod(shippingMethod);
			if (!isValid(order, Order.Delivery.class)) {
				return ERROR_VIEW;
			}
		} else {
			order.setFreight(BigDecimal.ZERO);
			order.setConsignee(null);
			order.setAreaName(null);
			order.setAddress(null);
			order.setZipCode(null);
			order.setPhone(null);
			order.setShippingMethodName(null);
			order.setArea(null);
			order.setShippingMethod(null);
			if (!isValid(order)) {
				return ERROR_VIEW;
			}
		}

		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.update(order, admin);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("methods", Payment.Method.values());
		model.addAttribute("refundsMethods", Refunds.Method.values());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		model.addAttribute("order", orderService.find(id));
		return "/admin/order/view";
	}

	/**
	 * 审核
	 */
	@RequestMapping(value = "/review", method = RequestMethod.POST)
	public String review(Long id, Boolean passed, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.pendingReview.equals(order.getStatus())) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.review(order, passed, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 收款
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String payment(Payment payment, Long orderId, Long paymentMethodId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null) {
			return ERROR_VIEW;
		}
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		if (!isValid(payment)) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		Member member = order.getMember();
		if (Payment.Method.deposit.equals(payment.getMethod()) && payment.getAmount().compareTo(member.getBalance()) > 0) {
			return ERROR_VIEW;
		}
		payment.setFee(BigDecimal.ZERO);
		payment.setOperator(admin);
		orderService.payment(order, payment, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + orderId;
	}

	/**
	 * 退款
	 */
	@RequestMapping(value = "/refunds", method = RequestMethod.POST)
	public String refunds(Refunds refunds, Long orderId, Long paymentMethodId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null || order.getRefundableAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return ERROR_VIEW;
		}
		refunds.setOrder(order);
		refunds.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		if (!isValid(refunds)) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		refunds.setOperator(admin);
		orderService.refunds(order, refunds, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + orderId;
	}

	/**
	 * 发货
	 */
	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	public String shipping(Shipping shipping, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long areaId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null || order.getShippableQuantity() <= 0) {
			return ERROR_VIEW;
		}
		boolean isDelivery = false;
		for (Iterator<ShippingItem> iterator = shipping.getShippingItems().iterator(); iterator.hasNext();) {
			ShippingItem shippingItem = iterator.next();
			if (shippingItem == null || StringUtils.isEmpty(shippingItem.getSn()) || shippingItem.getQuantity() == null || shippingItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null || shippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				return ERROR_VIEW;
			}
			Product product = orderItem.getProduct();
			if (product != null && shippingItem.getQuantity() > product.getStock()) {
				return ERROR_VIEW;
			}
			shippingItem.setName(orderItem.getName());
			shippingItem.setIsDelivery(orderItem.getIsDelivery());
			shippingItem.setProduct(product);
			shippingItem.setShipping(shipping);
			shippingItem.setSpecifications(orderItem.getSpecifications());
			if (orderItem.getIsDelivery()) {
				isDelivery = true;
			}
		}
		shipping.setOrder(order);
		shipping.setShippingMethod(shippingMethodService.find(shippingMethodId));
		shipping.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		shipping.setArea(areaService.find(areaId));
		if (isDelivery) {
			if (!isValid(shipping, Shipping.Delivery.class)) {
				return ERROR_VIEW;
			}
		} else {
			shipping.setShippingMethod((String) null);
			shipping.setDeliveryCorp((String) null);
			shipping.setDeliveryCorpUrl(null);
			shipping.setDeliveryCorpCode(null);
			shipping.setTrackingNo(null);
			shipping.setFreight(null);
			shipping.setConsignee(null);
			shipping.setArea((String) null);
			shipping.setAddress(null);
			shipping.setZipCode(null);
			shipping.setPhone(null);
			if (!isValid(shipping)) {
				return ERROR_VIEW;
			}
		}

		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		shipping.setOperator(admin);
		orderService.shipping(order, shipping, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + orderId;
	}

	/**
	 * 退货
	 */
	@RequestMapping(value = "/returns", method = RequestMethod.POST)
	public String returns(Returns returns, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long areaId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null || order.getReturnableQuantity() <= 0) {
			return ERROR_VIEW;
		}
		for (Iterator<ReturnsItem> iterator = returns.getReturnsItems().iterator(); iterator.hasNext();) {
			ReturnsItem returnsItem = iterator.next();
			if (returnsItem == null || StringUtils.isEmpty(returnsItem.getSn()) || returnsItem.getQuantity() == null || returnsItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem == null || returnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
				return ERROR_VIEW;
			}
			returnsItem.setName(orderItem.getName());
			returnsItem.setReturns(returns);
			returnsItem.setSpecifications(orderItem.getSpecifications());
		}
		returns.setOrder(order);
		returns.setShippingMethod(shippingMethodService.find(shippingMethodId));
		returns.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		returns.setArea(areaService.find(areaId));
		if (!isValid(returns)) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		returns.setOperator(admin);
		orderService.returns(order, returns, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + orderId;
	}

	/**
	 * 收货
	 */
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public String receive(Long id, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.shipped.equals(order.getStatus())) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.receive(order, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 完成
	 */
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public String complete(Long id, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.received.equals(order.getStatus())) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.complete(order, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 失败
	 */
	@RequestMapping(value = "/fail", method = RequestMethod.POST)
	public String fail(Long id, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingShipment.equals(order.getStatus()) && !Order.Status.shipped.equals(order.getStatus()) && !Order.Status.received.equals(order.getStatus()))) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			return ERROR_VIEW;
		}
		orderService.fail(order, admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model) {
		model.addAttribute("types", Order.Type.values());
		model.addAttribute("statuses", Order.Status.values());
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		model.addAttribute("memberUsername", memberUsername);
		model.addAttribute("isPendingReceive", isPendingReceive);
		model.addAttribute("isPendingRefunds", isPendingRefunds);
		model.addAttribute("isAllocatedStock", isAllocatedStock);
		model.addAttribute("hasExpired", hasExpired);

		Member member = memberService.findByUsername(memberUsername);
		if (StringUtils.isNotEmpty(memberUsername) && member == null) {
			model.addAttribute("page", Page.emptyPage(pageable));
		} else {
			model.addAttribute("page", orderService.findPage(type, status, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable));
		}
		return "/admin/order/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			Admin admin = adminService.getCurrent();
			for (Long id : ids) {
				Order order = orderService.find(id);
				if (order != null && orderService.isLocked(order, admin, true)) {
					return Message.error("admin.order.deleteLockedNotAllowed", order.getSn());
				}
			}
			orderService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}