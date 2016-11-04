/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.shop;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.shopxx.Message;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CartItem;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.service.CartItemService;
import net.shopxx.service.CartService;
import net.shopxx.service.MemberService;
import net.shopxx.service.ProductService;
import net.shopxx.util.WebUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 购物车
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("shopCartController")
@RequestMapping("/cart")
public class CartController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;

	/**
	 * 数量
	 */
	@RequestMapping(value = "/quantity", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Integer> quantity() {
		Map<String, Integer> data = new HashMap<String, Integer>();
		Cart cart = cartService.getCurrent();
		data.put("quantity", cart != null ? cart.getProductQuantity() : 0);
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	Message add(Long productId, Integer quantity, HttpServletRequest request, HttpServletResponse response) {
		if (quantity == null || quantity < 1) {
			return ERROR_MESSAGE;
		}
		Product product = productService.find(productId);
		if (product == null) {
			return Message.warn("shop.cart.productNotExist");
		}
		if (!Goods.Type.general.equals(product.getType())) {
			return Message.warn("shop.cart.productNotForSale");
		}
		if (!product.getIsMarketable()) {
			return Message.warn("shop.cart.productNotMarketable");
		}

		Cart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.contains(product)) {
				CartItem cartItem = cart.getCartItem(product);
				if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
					return Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY);
				}
				if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
					return Message.warn("shop.cart.productLowStock");
				}
			} else {
				if (Cart.MAX_CART_ITEM_COUNT != null && cart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
					return Message.warn("shop.cart.addCartItemCountNotAllowed", Cart.MAX_CART_ITEM_COUNT);
				}
				if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
					return Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY);
				}
				if (quantity > product.getAvailableStock()) {
					return Message.warn("shop.cart.productLowStock");
				}
			}
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY);
			}
			if (quantity > product.getAvailableStock()) {
				return Message.warn("shop.cart.productLowStock");
			}
		}
		cart = cartService.add(product, quantity);

		Member member = memberService.getCurrent();
		if (member == null) {
			WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
		}
		return Message.success("shop.cart.addSuccess", cart.getProductQuantity(), currency(cart.getEffectivePrice(), true, false));
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("cart", cartService.getCurrent());
		return "/shop/${theme}/cart/list";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> edit(Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		if (!cart.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExist"));
			return data;
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			data.put("message", Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
			return data;
		}
		Product product = cartItem.getProduct();
		if (quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.cart.productLowStock"));
			return data;
		}
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getSubtotal());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getProductQuantity());
		data.put("effectiveRewardPoint", cart.getEffectiveRewardPoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("giftNames", cart.getGiftNames());
		data.put("promotionNames", cart.getPromotionNames());
		return data;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		if (!cart.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExist"));
			return data;
		}
		cartItemService.delete(cartItem);
		cart.getCartItems().remove(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("isLowStock", cart.getIsLowStock());
		data.put("quantity", cart.getProductQuantity());
		data.put("effectiveRewardPoint", cart.getEffectiveRewardPoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("giftNames", cart.getGiftNames());
		data.put("promotionNames", cart.getPromotionNames());
		return data;
	}

	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	Message clear() {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart);
		return SUCCESS_MESSAGE;
	}

}