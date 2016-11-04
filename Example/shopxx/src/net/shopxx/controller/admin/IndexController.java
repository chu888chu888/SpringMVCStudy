/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.entity.Article;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Product;
import net.shopxx.service.ArticleService;
import net.shopxx.service.GoodsService;
import net.shopxx.service.SearchService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 索引
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminIndexController")
@RequestMapping("/admin/index")
public class IndexController extends BaseController {

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	/**
	 * 生成类型
	 */
	public enum GenerateType {
		/**
		 * 文章
		 */
		article,

		/**
		 * 商品
		 */
		goods
	}

	/**
	 * 生成索引
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		model.addAttribute("generateTypes", IndexController.GenerateType.values());
		return "/admin/index/generate";
	}

	/**
	 * 生成索引
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generate(IndexController.GenerateType generateType, Boolean isPurge, Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		if (first == null || first < 0) {
			first = 0;
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case article:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Article.class);
			}
			List<Article> articles = articleService.findList(first, count, null, null);
			for (Article article : articles) {
				searchService.index(article);
				generateCount++;
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Product.class);
			}
			List<Goods> goodsList = goodsService.findList(first, count, null, null);
			for (Goods goods : goodsList) {
				searchService.index(goods);
				generateCount++;
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
			break;
		}
		long endTime = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("first", first);
		data.put("generateCount", generateCount);
		data.put("generateTime", endTime - startTime);
		data.put("isCompleted", isCompleted);
		return data;
	}

}