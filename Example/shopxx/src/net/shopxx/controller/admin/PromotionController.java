/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.Goods;
import net.shopxx.entity.MemberRank;
import net.shopxx.entity.Product;
import net.shopxx.entity.Promotion;
import net.shopxx.service.CouponService;
import net.shopxx.service.MemberRankService;
import net.shopxx.service.ProductService;
import net.shopxx.service.PromotionService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 促销
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminPromotionController")
@RequestMapping("/admin/promotion")
public class PromotionController extends BaseController {

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	/**
	 * 检查价格运算表达式是否正确
	 */
	@RequestMapping(value = "/check_price_expression", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPriceExpression(String priceExpression) {
		if (StringUtils.isEmpty(priceExpression)) {
			return false;
		}
		return promotionService.isValidPriceExpression(priceExpression);
	}

	/**
	 * 检查积分运算表达式是否正确
	 */
	@RequestMapping(value = "/check_point_expression", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPointExpression(String pointExpression) {
		if (StringUtils.isEmpty(pointExpression)) {
			return false;
		}
		return promotionService.isValidPointExpression(pointExpression);
	}

	/**
	 * 赠品选择
	 */
	@RequestMapping(value = "/gift_select", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> giftSelect(@RequestParam("q") String keyword, Long[] excludeIds, @RequestParam("limit") Integer count) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isEmpty(keyword)) {
			return data;
		}
		Set<Product> excludes = new HashSet<Product>(productService.findList(excludeIds));
		List<Product> products = productService.search(Goods.Type.gift, keyword, excludes, count);
		for (Product product : products) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", product.getId());
			item.put("sn", product.getSn());
			item.put("name", product.getName());
			item.put("specifications", product.getSpecifications());
			item.put("url", product.getUrl());
			data.add(item);
		}
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("coupons", couponService.findAll());
		return "/admin/promotion/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Promotion promotion, Long[] memberRankIds, Long[] couponIds, Long[] giftIds, RedirectAttributes redirectAttributes) {
		promotion.setMemberRanks(new HashSet<MemberRank>(memberRankService.findList(memberRankIds)));
		promotion.setCoupons(new HashSet<Coupon>(couponService.findList(couponIds)));
		if (ArrayUtils.isNotEmpty(giftIds)) {
			List<Product> gifts = productService.findList(giftIds);
			CollectionUtils.filter(gifts, new Predicate() {
				public boolean evaluate(Object object) {
					Product gift = (Product) object;
					return gift != null && Goods.Type.gift.equals(gift.getType());
				}
			});
			promotion.setGifts(new HashSet<Product>(gifts));
		} else {
			promotion.setGifts(null);
		}
		if (!isValid(promotion)) {
			return ERROR_VIEW;
		}
		if (promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate())) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumQuantity() != null && promotion.getMaximumQuantity() != null && promotion.getMinimumQuantity() > promotion.getMaximumQuantity()) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumPrice() != null && promotion.getMaximumPrice() != null && promotion.getMinimumPrice().compareTo(promotion.getMaximumPrice()) > 0) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(promotion.getPriceExpression()) && !promotionService.isValidPriceExpression(promotion.getPriceExpression())) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(promotion.getPointExpression()) && !promotionService.isValidPointExpression(promotion.getPointExpression())) {
			return ERROR_VIEW;
		}
		promotion.setGoods(null);
		promotion.setProductCategories(null);
		promotionService.save(promotion);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("promotion", promotionService.find(id));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("coupons", couponService.findAll());
		return "/admin/promotion/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Promotion promotion, Long[] memberRankIds, Long[] couponIds, Long[] giftIds, RedirectAttributes redirectAttributes) {
		promotion.setMemberRanks(new HashSet<MemberRank>(memberRankService.findList(memberRankIds)));
		promotion.setCoupons(new HashSet<Coupon>(couponService.findList(couponIds)));
		if (ArrayUtils.isNotEmpty(giftIds)) {
			List<Product> gifts = productService.findList(giftIds);
			CollectionUtils.filter(gifts, new Predicate() {
				public boolean evaluate(Object object) {
					Product gift = (Product) object;
					return gift != null && Goods.Type.gift.equals(gift.getType());
				}
			});
			promotion.setGifts(new HashSet<Product>(gifts));
		} else {
			promotion.setGifts(null);
		}
		if (promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate())) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumQuantity() != null && promotion.getMaximumQuantity() != null && promotion.getMinimumQuantity() > promotion.getMaximumQuantity()) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumPrice() != null && promotion.getMaximumPrice() != null && promotion.getMinimumPrice().compareTo(promotion.getMaximumPrice()) > 0) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(promotion.getPriceExpression()) && !promotionService.isValidPriceExpression(promotion.getPriceExpression())) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(promotion.getPointExpression()) && !promotionService.isValidPointExpression(promotion.getPointExpression())) {
			return ERROR_VIEW;
		}
		promotionService.update(promotion, "goods", "productCategories");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", promotionService.findPage(pageable));
		return "/admin/promotion/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		promotionService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}