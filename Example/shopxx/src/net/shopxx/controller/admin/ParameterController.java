/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import javax.annotation.Resource;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.Parameter;
import net.shopxx.service.ParameterService;
import net.shopxx.service.ProductCategoryService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 参数
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminParameterController")
@RequestMapping("/admin/parameter")
public class ParameterController extends BaseController {

	@Resource(name = "parameterServiceImpl")
	private ParameterService parameterService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long sampleId, ModelMap model) {
		model.addAttribute("sample", parameterService.find(sampleId));
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		return "/admin/parameter/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Parameter parameter, Long productCategoryId, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(parameter.getNames(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String name = (String) object;
				return StringUtils.isNotEmpty(name);
			}
		}));
		parameter.setProductCategory(productCategoryService.find(productCategoryId));
		if (!isValid(parameter, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		parameterService.save(parameter);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("parameter", parameterService.find(id));
		return "/admin/parameter/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Parameter parameter, RedirectAttributes redirectAttributes) {
		CollectionUtils.filter(parameter.getNames(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String name = (String) object;
				return StringUtils.isNotEmpty(name);
			}
		}));
		if (!isValid(parameter, BaseEntity.Update.class)) {
			return ERROR_VIEW;
		}
		parameterService.update(parameter, "productCategory");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", parameterService.findPage(pageable));
		return "/admin/parameter/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		parameterService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}