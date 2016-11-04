/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.shop;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.entity.Consultation;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Member;
import net.shopxx.exception.ResourceNotFoundException;
import net.shopxx.service.CaptchaService;
import net.shopxx.service.ConsultationService;
import net.shopxx.service.GoodsService;
import net.shopxx.service.MemberService;
import net.shopxx.util.SystemUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 咨询
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("shopConsultationController")
@RequestMapping("/consultation")
public class ConsultationController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	/**
	 * 发表
	 */
	@RequestMapping(value = "/add/{goodsId}", method = RequestMethod.GET)
	public String add(@PathVariable Long goodsId, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsConsultationEnabled()) {
			throw new ResourceNotFoundException();
		}
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		model.addAttribute("goods", goods);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/${theme}/consultation/add";
	}

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{goodsId}", method = RequestMethod.GET)
	public String content(@PathVariable Long goodsId, Integer pageNumber, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsConsultationEnabled()) {
			throw new ResourceNotFoundException();
		}
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("goods", goods);
		model.addAttribute("page", consultationService.findPage(null, goods, true, pageable));
		return "/shop/${theme}/consultation/content";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody
	Message save(String captchaId, String captcha, Long goodsId, String content, HttpServletRequest request) {
		if (!captchaService.isValid(Setting.CaptchaType.consultation, captchaId, captcha)) {
			return Message.error("shop.captcha.invalid");
		}
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsConsultationEnabled()) {
			return Message.error("shop.consultation.disabled");
		}
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			return ERROR_MESSAGE;
		}
		if (!isValid(Consultation.class, "content", content)) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (!Setting.ConsultationAuthority.anyone.equals(setting.getConsultationAuthority()) && member == null) {
			return Message.error("shop.consultation.accessDenied");
		}

		Consultation consultation = new Consultation();
		consultation.setContent(content);
		consultation.setIp(request.getRemoteAddr());
		consultation.setMember(member);
		consultation.setGoods(goods);
		consultation.setForConsultation(null);
		consultation.setReplyConsultations(null);
		if (setting.getIsConsultationCheck()) {
			consultation.setIsShow(false);
			consultationService.save(consultation);
			return Message.success("shop.consultation.check");
		} else {
			consultation.setIsShow(true);
			consultationService.save(consultation);
			return Message.success("shop.consultation.success");
		}
	}

}