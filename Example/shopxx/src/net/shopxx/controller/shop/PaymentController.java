/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.shop;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.shopxx.Setting;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.PaymentLog;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.plugin.PaymentPlugin;
import net.shopxx.service.MemberService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentLogService;
import net.shopxx.service.PluginService;
import net.shopxx.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 支付
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("shopPaymentController")
@RequestMapping("/payment")
public class PaymentController extends BaseController {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "paymentLogServiceImpl")
	private PaymentLogService paymentLogService;

	/**
	 * 插件提交
	 */
	@RequestMapping(value = "/plugin_submit", method = RequestMethod.POST)
	public String pluginSubmit(PaymentLog.Type type, String paymentPluginId, String sn, BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if (type == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		switch (type) {
		case recharge: {
			if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				return ERROR_VIEW;
			}
			PaymentLog paymentLog = new PaymentLog();
			paymentLog.setSn(null);
			paymentLog.setType(type);
			paymentLog.setStatus(PaymentLog.Status.wait);
			paymentLog.setFee(paymentPlugin.calculateFee(amount));
			paymentLog.setAmount(paymentPlugin.calculateAmount(amount));
			paymentLog.setPaymentPluginId(paymentPluginId);
			paymentLog.setPaymentPluginName(paymentPlugin.getName());
			paymentLog.setMember(member);
			paymentLog.setOrder(null);
			paymentLogService.save(paymentLog);
			model.addAttribute("parameterMap", paymentPlugin.getParameterMap(paymentLog.getSn(), message("shop.payment.rechargeDescription"), request));
			break;
		}
		case payment: {
			Order order = orderService.findBySn(sn);
			if (order == null || !member.equals(order.getMember()) || orderService.isLocked(order, member, true)) {
				return ERROR_VIEW;
			}
			if (order.getPaymentMethod() == null || !PaymentMethod.Method.online.equals(order.getPaymentMethod().getMethod())) {
				return ERROR_VIEW;
			}
			if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
				return ERROR_VIEW;
			}
			PaymentLog paymentLog = new PaymentLog();
			paymentLog.setSn(null);
			paymentLog.setType(type);
			paymentLog.setStatus(PaymentLog.Status.wait);
			paymentLog.setFee(paymentPlugin.calculateFee(order.getAmountPayable()));
			paymentLog.setAmount(paymentPlugin.calculateAmount(order.getAmountPayable()));
			paymentLog.setPaymentPluginId(paymentPluginId);
			paymentLog.setPaymentPluginName(paymentPlugin.getName());
			paymentLog.setOrder(order);
			paymentLog.setMember(null);
			paymentLogService.save(paymentLog);
			model.addAttribute("parameterMap", paymentPlugin.getParameterMap(paymentLog.getSn(), message("shop.payment.paymentDescription", order.getSn()), request));
			break;
		}
		}
		model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
		model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
		model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
		if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
			response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		return "/shop/${theme}/payment/plugin_submit";
	}

	/**
	 * 插件通知
	 */
	@RequestMapping("/plugin_notify/{pluginId}/{notifyMethod}")
	public String pluginNotify(@PathVariable String pluginId, @PathVariable PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request, ModelMap model) {
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(pluginId);
		if (paymentPlugin != null && paymentPlugin.verifyNotify(notifyMethod, request)) {
			String sn = paymentPlugin.getSn(request);
			PaymentLog paymentLog = paymentLogService.findBySn(sn);
			if (paymentLog != null) {
				paymentLogService.handle(paymentLog);
				model.addAttribute("paymentLog", paymentLog);
				model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(notifyMethod, request));
			}
		}
		return "/shop/${theme}/payment/plugin_notify";
	}

}