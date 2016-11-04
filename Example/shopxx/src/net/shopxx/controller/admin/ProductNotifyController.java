/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.ProductNotify;
import net.shopxx.service.ProductNotifyService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 到货通知
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminProductNotifyntroller")
@RequestMapping("/admin/product_notify")
public class ProductNotifyController extends BaseController {

	@Resource(name = "productNotifyServiceImpl")
	private ProductNotifyService productNotifyService;

	/**
	 * 发送到货通知
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public @ResponseBody
	Message send(Long[] ids) {
		List<ProductNotify> productNotifies = productNotifyService.findList(ids);
		int count = productNotifyService.send(productNotifies);
		return Message.success("admin.productNotify.sentSuccess", count);
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable, ModelMap model) {
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("hasSent", hasSent);
		model.addAttribute("page", productNotifyService.findPage(null, isMarketable, isOutOfStock, hasSent, pageable));
		return "/admin/product_notify/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		productNotifyService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}