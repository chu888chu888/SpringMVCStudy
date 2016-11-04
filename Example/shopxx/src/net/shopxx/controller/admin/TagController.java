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
import net.shopxx.entity.Tag;
import net.shopxx.service.TagService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 标签
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminTagController")
@RequestMapping("/admin/tag")
public class TagController extends BaseController {

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", Tag.Type.values());
		return "/admin/tag/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Tag tag, RedirectAttributes redirectAttributes) {
		if (!isValid(tag, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		tag.setArticles(null);
		tag.setGoods(null);
		tagService.save(tag);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", Tag.Type.values());
		model.addAttribute("tag", tagService.find(id));
		return "/admin/tag/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Tag tag, RedirectAttributes redirectAttributes) {
		if (!isValid(tag)) {
			return ERROR_VIEW;
		}
		tagService.update(tag, "type", "articles", "goods");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tagService.findPage(pageable));
		return "/admin/tag/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		tagService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}