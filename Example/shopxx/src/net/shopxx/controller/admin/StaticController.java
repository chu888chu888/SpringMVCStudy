/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.entity.Article;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.entity.Goods;
import net.shopxx.entity.ProductCategory;
import net.shopxx.service.ArticleCategoryService;
import net.shopxx.service.ArticleService;
import net.shopxx.service.CacheService;
import net.shopxx.service.GoodsService;
import net.shopxx.service.ProductCategoryService;
import net.shopxx.service.StaticService;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 静态化
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminStaticController")
@RequestMapping("/admin/static")
public class StaticController extends BaseController {

	/**
	 * 生成类型
	 */
	public enum GenerateType {
		/**
		 * 首页
		 */
		index,

		/**
		 * 文章
		 */
		article,

		/**
		 * 商品
		 */
		goods,

		/**
		 * 其它
		 */
		other
	}

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;

	/**
	 * 生成静态
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		model.addAttribute("generateTypes", StaticController.GenerateType.values());
		model.addAttribute("defaultBeginDate", DateUtils.addDays(new Date(), -7));
		model.addAttribute("defaultEndDate", new Date());
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		return "/admin/static/generate";
	}

	/**
	 * 生成静态
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generate(StaticController.GenerateType generateType, Long articleCategoryId, Long productCategoryId, Date beginDate, Date endDate, Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		if (first == null || first < 0) {
			first = 0;
			cacheService.clear();
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case index:
			generateCount = staticService.generateIndex();
			break;
		case article:
			ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);
			List<Article> articles = articleService.findList(articleCategory, true, null, beginDate, endDate, first, count);
			for (Article article : articles) {
				generateCount += staticService.generate(article);
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			ProductCategory productCategory = productCategoryService.find(productCategoryId);
			List<Goods> goodsList = goodsService.findList(productCategory, true, null, beginDate, endDate, first, count);
			for (Goods goods : goodsList) {
				generateCount += staticService.generate(goods);
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
			break;
		case other:
			generateCount = staticService.generateOther();
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