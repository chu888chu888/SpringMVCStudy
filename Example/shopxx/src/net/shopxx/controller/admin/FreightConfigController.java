/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Area;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.FreightConfig;
import net.shopxx.entity.ShippingMethod;
import net.shopxx.service.AreaService;
import net.shopxx.service.FreightConfigService;
import net.shopxx.service.ShippingMethodService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 运费配置
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminFreightConfigController")
@RequestMapping("/admin/freight_config")
public class FreightConfigController extends BaseController {

	@Resource(name = "freightConfigServiceImpl")
	private FreightConfigService freightConfigService;
	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 检查地区是否唯一
	 */
	@RequestMapping(value = "/check_area", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkArea(Long shippingMethodId, Long previousAreaId, Long areaId) {
		if (areaId == null) {
			return false;
		}
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		Area previousArea = areaService.find(previousAreaId);
		Area area = areaService.find(areaId);
		return freightConfigService.unique(shippingMethod, previousArea, area);
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long shippingMethodId, ModelMap model) {
		model.addAttribute("shippingMethod", shippingMethodService.find(shippingMethodId));
		return "/admin/freight_config/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(FreightConfig freightConfig, Long shippingMethodId, Long areaId, RedirectAttributes redirectAttributes) {
		Area area = areaService.find(areaId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		freightConfig.setArea(area);
		freightConfig.setShippingMethod(shippingMethod);
		if (!isValid(freightConfig, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		if (freightConfigService.exists(shippingMethod, area)) {
			return ERROR_VIEW;
		}
		freightConfigService.save(freightConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("freightConfig", freightConfigService.find(id));
		return "/admin/freight_config/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(FreightConfig freightConfig, Long id, Long areaId, RedirectAttributes redirectAttributes) {
		Area area = areaService.find(areaId);
		freightConfig.setArea(area);
		if (!isValid(freightConfig, BaseEntity.Update.class)) {
			return ERROR_VIEW;
		}
		FreightConfig pFreightConfig = freightConfigService.find(id);
		if (!freightConfigService.unique(pFreightConfig.getShippingMethod(), pFreightConfig.getArea(), area)) {
			return ERROR_VIEW;
		}
		freightConfigService.update(freightConfig, "shippingMethod");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, Long shippingMethodId, ModelMap model) {
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		model.addAttribute("shippingMethod", shippingMethod);
		model.addAttribute("page", freightConfigService.findPage(shippingMethod, pageable));
		return "/admin/freight_config/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		freightConfigService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}