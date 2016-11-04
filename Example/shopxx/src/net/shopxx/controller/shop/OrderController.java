/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CartItem;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Invoice;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Product;
import net.shopxx.entity.Receiver;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.service.AreaService;
import net.shopxx.service.CartService;
import net.shopxx.service.CouponCodeService;
import net.shopxx.service.MemberService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentMethodService;
import net.shopxx.service.PluginService;
import net.shopxx.service.ProductService;
import net.shopxx.service.ReceiverService;
import net.shopxx.service.ShippingMethodService;

import org.apache.commons.collections.CollectionUtils;
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
@Controller("shopOrderController")
@RequestMapping("/order")
public class OrderController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	/**
	 * 检查积分兑换
	 */
	@RequestMapping(value = "/check_exchange", method = RequestMethod.GET)
	public @ResponseBody
	Message checkExchange(Long productId, Integer quantity) {
		if (quantity == null || quantity < 1) {
			return ERROR_MESSAGE;
		}
		Product product = productService.find(productId);
		if (product == null) {
			return ERROR_MESSAGE;
		}
		if (!Goods.Type.exchange.equals(product.getType())) {
			return ERROR_MESSAGE;
		}
		if (!product.getIsMarketable()) {
			return Message.warn("shop.order.productNotMarketable");
		}
		if (quantity > product.getAvailableStock()) {
			return Message.warn("shop.order.productLowStock");
		}
		Member member = memberService.getCurrent();
		if (member.getPoint() < product.getExchangePoint() * quantity) {
			return Message.warn("shop.order.lowPoint");
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 保存收货地址
	 */
	@RequestMapping(value = "/save_receiver", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveReceiver(Receiver receiver, Long areaId) {
		Map<String, Object> data = new HashMap<String, Object>();
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			data.put("message", Message.error("shop.order.addReceiverCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
			return data;
		}
		receiver.setAreaName(null);
		receiver.setMember(member);
		receiverService.save(receiver);
		data.put("message", SUCCESS_MESSAGE);
		data.put("id", receiver.getId());
		data.put("consignee", receiver.getConsignee());
		data.put("areaName", receiver.getAreaName());
		data.put("address", receiver.getAddress());
		data.put("zipCode", receiver.getZipCode());
		data.put("phone", receiver.getPhone());
		return data;
	}

	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody
	void lock(String sn) {
		Order order = orderService.findBySn(sn);
		Member member = memberService.getCurrent();
		if (order != null && member.equals(order.getMember()) && order.getPaymentMethod() != null && PaymentMethod.Method.online.equals(order.getPaymentMethod().getMethod()) && order.getAmountPayable().compareTo(BigDecimal.ZERO) > 0) {
			orderService.lock(order, member);
		}
	}

	/**
	 * 检查等待付款
	 */
	@RequestMapping(value = "/check_pending_payment", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPendingPayment(String sn) {
		Order order = orderService.findBySn(sn);
		Member member = memberService.getCurrent();
		return order != null && member.equals(order.getMember()) && order.getPaymentMethod() != null && PaymentMethod.Method.online.equals(order.getPaymentMethod().getMethod()) && order.getAmountPayable().compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * 检查优惠券
	 */
	@RequestMapping(value = "/check_coupon", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> checkCoupon(String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (!cart.isCouponAllowed()) {
			data.put("message", Message.warn("shop.order.couponNotAllowed"));
			return data;
		}
		CouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && couponCode.getCoupon() != null) {
			Coupon coupon = couponCode.getCoupon();
			if (couponCode.getIsUsed()) {
				data.put("message", Message.warn("shop.order.couponCodeUsed"));
				return data;
			}
			if (!coupon.getIsEnabled()) {
				data.put("message", Message.warn("shop.order.couponDisabled"));
				return data;
			}
			if (!coupon.hasBegun()) {
				data.put("message", Message.warn("shop.order.couponNotBegin"));
				return data;
			}
			if (coupon.hasExpired()) {
				data.put("message", Message.warn("shop.order.couponHasExpired"));
				return data;
			}
			if (!cart.isValid(coupon)) {
				data.put("message", Message.warn("shop.order.couponInvalid"));
				return data;
			}
			data.put("message", SUCCESS_MESSAGE);
			data.put("couponName", coupon.getName());
			return data;
		} else {
			data.put("message", Message.warn("shop.order.couponCodeNotExist"));
			return data;
		}
	}

	/**
	 * 结算
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout(ModelMap model) {
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return "redirect:/cart/list.jhtml";
		}

		Member member = memberService.getCurrent();
		Receiver defaultReceiver = receiverService.findDefault(member);
		Order order = orderService.generate(Order.Type.general, cart, defaultReceiver, null, null, null, null, null, null);
		model.addAttribute("order", order);
		model.addAttribute("defaultReceiver", defaultReceiver);
		model.addAttribute("cartToken", cart.getToken());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		return "/shop/${theme}/order/checkout";
	}

	/**
	 * 结算
	 */
	@RequestMapping(value = "/checkout", params = "type=exchange", method = RequestMethod.GET)
	public String checkout(Long productId, Integer quantity, ModelMap model) {
		if (quantity == null || quantity < 1) {
			return ERROR_VIEW;
		}
		Product product = productService.find(productId);
		if (product == null) {
			return ERROR_VIEW;
		}
		if (!Goods.Type.exchange.equals(product.getType())) {
			return ERROR_VIEW;
		}
		if (!product.getIsMarketable()) {
			return ERROR_VIEW;
		}
		if (quantity > product.getAvailableStock()) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (member.getPoint() < product.getExchangePoint() * quantity) {
			return ERROR_VIEW;
		}
		Set<CartItem> cartItems = new HashSet<CartItem>();
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItems.add(cartItem);
		Cart cart = new Cart();
		cart.setMember(member);
		cart.setCartItems(cartItems);
		Receiver defaultReceiver = receiverService.findDefault(member);
		Order order = orderService.generate(Order.Type.exchange, cart, defaultReceiver, null, null, null, null, null, null);
		model.addAttribute("productId", productId);
		model.addAttribute("quantity", quantity);
		model.addAttribute("order", order);
		model.addAttribute("defaultReceiver", defaultReceiver);
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		return "/shop/${theme}/order/checkout";
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> calculate(Long receiverId, Long paymentMethodId, Long shippingMethodId, String code, String invoiceTitle, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		Receiver receiver = receiverService.find(receiverId);
		if (receiver != null && !member.equals(receiver.getMember())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}

		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		CouponCode couponCode = couponCodeService.findByCode(code);
		Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) ? new Invoice(invoiceTitle, null) : null;
		Order order = orderService.generate(Order.Type.general, cart, receiver, paymentMethod, shippingMethod, couponCode, invoice, balance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("price", order.getPrice());
		data.put("fee", order.getFee());
		data.put("freight", order.getFreight());
		data.put("tax", order.getTax());
		data.put("promotionDiscount", order.getPromotionDiscount());
		data.put("couponDiscount", order.getCouponDiscount());
		data.put("amount", order.getAmount());
		data.put("amountPayable", order.getAmountPayable());
		return data;
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", params = "type=exchange", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> calculate(Long productId, Integer quantity, Long receiverId, Long paymentMethodId, Long shippingMethodId, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Product product = productService.find(productId);
		if (product == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		Receiver receiver = receiverService.find(receiverId);
		if (receiver != null && !member.equals(receiver.getMember())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		Set<CartItem> cartItems = new HashSet<CartItem>();
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItems.add(cartItem);
		Cart cart = new Cart();
		cart.setMember(member);
		cart.setCartItems(cartItems);
		Order order = orderService.generate(Order.Type.general, cart, receiver, paymentMethod, shippingMethod, null, null, balance, null);

		data.put("message", SUCCESS_MESSAGE);
		data.put("price", order.getPrice());
		data.put("fee", order.getFee());
		data.put("freight", order.getFreight());
		data.put("tax", order.getTax());
		data.put("promotionDiscount", order.getPromotionDiscount());
		data.put("couponDiscount", order.getCouponDiscount());
		data.put("amount", order.getAmount());
		data.put("amountPayable", order.getAmountPayable());
		return data;
	}

	/**
	 * 创建
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> create(String cartToken, Long receiverId, Long paymentMethodId, Long shippingMethodId, String code, String invoiceTitle, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (!StringUtils.equals(cart.getToken(), cartToken)) {
			data.put("message", Message.warn("shop.order.cartHasChanged"));
			return data;
		}
		if (cart.hasNotMarketable()) {
			data.put("message", Message.warn("shop.order.hasNotMarketable"));
			return data;
		}
		if (cart.getIsLowStock()) {
			data.put("message", Message.warn("shop.order.cartLowStock"));
			return data;
		}
		Member member = memberService.getCurrent();
		Receiver receiver = null;
		ShippingMethod shippingMethod = null;
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		if (cart.getIsDelivery()) {
			receiver = receiverService.find(receiverId);
			if (receiver == null || !member.equals(receiver.getMember())) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
			shippingMethod = shippingMethodService.find(shippingMethodId);
			if (shippingMethod == null) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
		}
		CouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && !cart.isValid(couponCode)) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}
		Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) ? new Invoice(invoiceTitle, null) : null;
		Order order = orderService.create(Order.Type.general, cart, receiver, paymentMethod, shippingMethod, couponCode, invoice, balance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("sn", order.getSn());
		return data;
	}

	/**
	 * 创建
	 */
	@RequestMapping(value = "/create", params = "type=exchange", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> create(Long productId, Integer quantity, Long receiverId, Long paymentMethodId, Long shippingMethodId, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Product product = productService.find(productId);
		if (product == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (!Goods.Type.exchange.equals(product.getType())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (!product.getIsMarketable()) {
			data.put("message", Message.warn("shop.order.productNotMarketable"));
			return data;
		}
		if (quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.order.productLowStock"));
			return data;
		}
		Member member = memberService.getCurrent();
		Receiver receiver = null;
		ShippingMethod shippingMethod = null;
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		if (product.getIsDelivery()) {
			receiver = receiverService.find(receiverId);
			if (receiver == null || !member.equals(receiver.getMember())) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
			shippingMethod = shippingMethodService.find(shippingMethodId);
			if (shippingMethod == null) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
		}
		if (member.getPoint() < product.getExchangePoint() * quantity) {
			data.put("message", Message.warn("shop.order.lowPoint"));
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}
		Set<CartItem> cartItems = new HashSet<CartItem>();
		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItems.add(cartItem);
		Cart cart = new Cart();
		cart.setMember(member);
		cart.setCartItems(cartItems);

		Order order = orderService.create(Order.Type.exchange, cart, receiver, paymentMethod, shippingMethod, null, null, balance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("sn", order.getSn());
		return data;
	}

	/**
	 * 支付
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(String sn, ModelMap model, RedirectAttributes redirectAttributes) {
		Order order = orderService.findBySn(sn);
		Member member = memberService.getCurrent();
		if (order == null || !member.equals(order.getMember()) || order.getPaymentMethod() == null || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			return ERROR_VIEW;
		}
		if (PaymentMethod.Method.online.equals(order.getPaymentMethod().getMethod())) {
			if (orderService.isLocked(order, member, true)) {
				addFlashMessage(redirectAttributes, Message.warn("shop.order.locked"));
				return "redirect:/member/order/view.jhtml?sn=" + sn + ".jhtml";
			}
			List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
			if (CollectionUtils.isNotEmpty(paymentPlugins)) {
				PaymentPlugin defaultPaymentPlugin = paymentPlugins.get(0);
				model.addAttribute("fee", defaultPaymentPlugin.calculateFee(order.getAmountPayable()));
				model.addAttribute("amount", defaultPaymentPlugin.calculateAmount(order.getAmountPayable()));
				model.addAttribute("defaultPaymentPlugin", defaultPaymentPlugin);
				model.addAttribute("paymentPlugins", paymentPlugins);
			}
		}
		model.addAttribute("order", order);
		return "/shop/${theme}/order/payment";
	}

	/**
	 * 计算支付金额
	 */
	@RequestMapping(value = "/calculate_amount", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> calculateAmount(String paymentPluginId, String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Order order = orderService.findBySn(sn);
		Member member = memberService.getCurrent();
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (order == null || !member.equals(order.getMember()) || paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", paymentPlugin.calculateFee(order.getAmountPayable()));
		data.put("amount", paymentPlugin.calculateAmount(order.getAmountPayable()));
		return data;
	}

}