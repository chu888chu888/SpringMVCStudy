/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.shop.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Shipping;
import net.shopxx.service.MemberService;
import net.shopxx.service.OrderService;
import net.shopxx.service.ShippingService;
import net.shopxx.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 订单
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("shopMemberOrderController")
@RequestMapping("/member/order")
public class OrderController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

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
		Member member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			return ERROR_MESSAGE;
		}
		if (orderService.isLocked(order, member, true)) {
			return Message.warn("shop.member.order.locked");
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 物流动态
	 */
	@RequestMapping(value = "/transit_step", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> transitStep(String shippingSn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Shipping shipping = shippingService.findBySn(shippingSn);
		if (shipping == null || shipping.getOrder() == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(shipping.getOrder().getMember())) {
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
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", orderService.findPage(null, null, member, null, null, null, null, null, null, null, pageable));
		return "/shop/${theme}/member/order/list";
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(String sn, ModelMap model) {
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		model.addAttribute("order", order);
		return "/shop/${theme}/member/order/view";
	}

	/**
	 * 取消
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody
	Message cancel(String sn) {
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			return ERROR_MESSAGE;
		}
		if (order.hasExpired() || (!Order.Status.pendingPayment.equals(order.getStatus()) && !Order.Status.pendingReview.equals(order.getStatus()))) {
			return ERROR_MESSAGE;
		}
		if (orderService.isLocked(order, member, true)) {
			return Message.warn("shop.member.order.locked");
		}
		orderService.cancel(order);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 收货
	 */
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public @ResponseBody
	Message receive(String sn) {
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			return ERROR_MESSAGE;
		}
		if (order.hasExpired() || !Order.Status.shipped.equals(order.getStatus())) {
			return ERROR_MESSAGE;
		}
		if (orderService.isLocked(order, member, true)) {
			return Message.warn("shop.member.order.locked");
		}
		orderService.receive(order, null);
		return SUCCESS_MESSAGE;
	}

}