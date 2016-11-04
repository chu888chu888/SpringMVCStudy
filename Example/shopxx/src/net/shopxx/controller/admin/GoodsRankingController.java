/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import javax.annotation.Resource;

import net.shopxx.Pageable;
import net.shopxx.entity.Goods;
import net.shopxx.service.GoodsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 商品排名
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminGoodsRankingController")
@RequestMapping("/admin/goods_ranking")
public class GoodsRankingController extends BaseController {

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Goods.RankingType rankingType, Pageable pageable, Model model) {
		if (rankingType == null) {
			rankingType = Goods.RankingType.sales;
		}
		model.addAttribute("rankingTypes", Goods.RankingType.values());
		model.addAttribute("rankingType", rankingType);
		model.addAttribute("page", goodsService.findPage(rankingType, pageable));
		return "/admin/goods_ranking/list";
	}

}