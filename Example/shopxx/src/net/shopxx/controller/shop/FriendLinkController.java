/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.shop;

import javax.annotation.Resource;

import net.shopxx.entity.FriendLink;
import net.shopxx.service.FriendLinkService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 友情链接
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("shopFriendLinkController")
@RequestMapping("/friend_link")
public class FriendLinkController extends BaseController {

	@Resource(name = "friendLinkServiceImpl")
	private FriendLinkService friendLinkService;

	/**
	 * 首页
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("textFriendLinks", friendLinkService.findList(FriendLink.Type.text));
		model.addAttribute("imageFriendLinks", friendLinkService.findList(FriendLink.Type.image));
		return "/shop/${theme}/friend_link/index";
	}

}