/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.shop.member;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Member;
import net.shopxx.service.GoodsService;
import net.shopxx.service.MemberService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 商品收藏
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("shopMemberFavoriteController")
@RequestMapping("/member/favorite")
public class FavoriteController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	Message add(Long goodsId) {
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			return ERROR_MESSAGE;
		}

		Member member = memberService.getCurrent();
		if (member.getFavoriteGoods().contains(goods)) {
			return Message.warn("shop.member.favorite.exist");
		}
		if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteGoods().size() >= Member.MAX_FAVORITE_COUNT) {
			return Message.warn("shop.member.favorite.addCountNotAllowed", Member.MAX_FAVORITE_COUNT);
		}
		member.getFavoriteGoods().add(goods);
		memberService.update(member);
		return Message.success("shop.member.favorite.success");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", goodsService.findPage(member, pageable));
		return "/shop/${theme}/member/favorite/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Goods goods = goodsService.find(id);
		if (goods == null) {
			return ERROR_MESSAGE;
		}

		Member member = memberService.getCurrent();
		if (!member.getFavoriteGoods().contains(goods)) {
			return ERROR_MESSAGE;
		}
		member.getFavoriteGoods().remove(goods);
		memberService.update(member);
		return SUCCESS_MESSAGE;
	}

}