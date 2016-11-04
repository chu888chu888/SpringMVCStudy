/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.Pageable;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Product;
import net.shopxx.entity.StockLog;
import net.shopxx.service.AdminService;
import net.shopxx.service.ProductService;
import net.shopxx.service.StockLogService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 库存
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminStockController")
@RequestMapping("/admin/stock")
public class StockController extends BaseController {

	@Resource(name = "stockLogServiceImpl")
	private StockLogService stockLogService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 商品选择
	 */
	@RequestMapping(value = "/product_select", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> productSelect(@RequestParam("q") String keyword, @RequestParam("limit") Integer count) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isEmpty(keyword)) {
			return data;
		}
		List<Product> products = productService.search(null, keyword, null, count);
		for (Product product : products) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", product.getId());
			item.put("sn", product.getSn());
			item.put("name", product.getName());
			item.put("stock", product.getStock());
			item.put("allocatedStock", product.getAllocatedStock());
			item.put("specifications", product.getSpecifications());
			data.add(item);
		}
		return data;
	}

	/**
	 * 入库
	 */
	@RequestMapping(value = "/stock_in", method = RequestMethod.GET)
	public String stockIn(Long productId, ModelMap model) {
		model.addAttribute("product", productService.find(productId));
		return "/admin/stock/stock_in";
	}

	/**
	 * 入库
	 */
	@RequestMapping(value = "/stock_in", method = RequestMethod.POST)
	public String stockIn(Long productId, Integer quantity, String memo, RedirectAttributes redirectAttributes) {
		Product product = productService.find(productId);
		if (product == null) {
			return ERROR_VIEW;
		}
		if (quantity == null || quantity <= 0) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		productService.addStock(product, quantity, StockLog.Type.stockIn, admin, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log.jhtml";
	}

	/**
	 * 出库
	 */
	@RequestMapping(value = "/stock_out", method = RequestMethod.GET)
	public String stockOut(Long productId, ModelMap model) {
		model.addAttribute("product", productService.find(productId));
		return "/admin/stock/stock_out";
	}

	/**
	 * 出库
	 */
	@RequestMapping(value = "/stock_out", method = RequestMethod.POST)
	public String stockOut(Long productId, Integer quantity, String memo, RedirectAttributes redirectAttributes) {
		Product product = productService.find(productId);
		if (product == null) {
			return ERROR_VIEW;
		}
		if (quantity == null || quantity <= 0) {
			return ERROR_VIEW;
		}
		if (product.getStock() - quantity < 0) {
			return ERROR_VIEW;
		}
		Admin admin = adminService.getCurrent();
		productService.addStock(product, -quantity, StockLog.Type.stockOut, admin, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log.jhtml";
	}

	/**
	 * 记录
	 */
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Pageable pageable, ModelMap model) {
		model.addAttribute("page", stockLogService.findPage(pageable));
		return "/admin/stock/log";
	}

}