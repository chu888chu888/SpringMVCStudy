/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.dao.CartDao;
import net.shopxx.dao.CartItemDao;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CartItem;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.service.CartService;
import net.shopxx.service.MemberService;
import net.shopxx.util.WebUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Service - 购物车
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("cartServiceImpl")
public class CartServiceImpl extends BaseServiceImpl<Cart, Long> implements CartService {

	@Resource(name = "cartDaoImpl")
	private CartDao cartDao;
	@Resource(name = "cartItemDaoImpl")
	private CartItemDao cartItemDao;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	public Cart getCurrent() {
		Cart cart;
		Member member = memberService.getCurrent(true);
		if (member != null) {
			cart = member.getCart();
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = requestAttributes != null ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
			if (request == null) {
				return null;
			}
			String key = WebUtils.getCookie(request, Cart.KEY_COOKIE_NAME);
			cart = cartDao.findByKey(key);
		}
		if (cart != null) {
			Date expire = DateUtils.addSeconds(new Date(), Cart.TIMEOUT);
			if (!DateUtils.isSameDay(cart.getExpire(), expire)) {
				cart.setExpire(expire);
			}
		}
		return cart;
	}

	public Cart add(Product product, int quantity) {
		Assert.notNull(product);
		Assert.state(quantity > 0);

		Cart cart = getCurrent();
		if (cart == null) {
			cart = new Cart();
			Member member = memberService.getCurrent();
			if (member != null && member.getCart() == null) {
				cart.setMember(member);
			}
			cartDao.persist(cart);
		}
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			cartItem.add(quantity);
		} else {
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItemDao.persist(cartItem);
			cart.getCartItems().add(cartItem);
		}
		return cart;
	}

	public void merge(Member member, Cart cart) {
		if (member == null || cart == null || cart.getMember() != null) {
			return;
		}
		Cart memberCart = member.getCart();
		if (memberCart != null) {
			if (cart.getCartItems() != null) {
				for (CartItem cartItem : cart.getCartItems()) {
					Product product = cartItem.getProduct();
					if (memberCart.contains(product)) {
						CartItem memberCartItem = memberCart.getCartItem(product);
						if (CartItem.MAX_QUANTITY != null && memberCartItem.getQuantity() + cartItem.getQuantity() > CartItem.MAX_QUANTITY) {
							continue;
						}
						memberCartItem.add(cartItem.getQuantity());
					} else {
						if (Cart.MAX_CART_ITEM_COUNT != null && memberCart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
							continue;
						}
						if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() > CartItem.MAX_QUANTITY) {
							continue;
						}
						CartItem item = new CartItem();
						item.setQuantity(cartItem.getQuantity());
						item.setProduct(cartItem.getProduct());
						item.setCart(memberCart);
						cartItemDao.persist(item);
						memberCart.getCartItems().add(cartItem);
					}
				}
			}
			cartDao.remove(cart);
		} else {
			cart.setMember(member);
			member.setCart(cart);
		}
	}

	public void evictExpired() {
		while (true) {
			List<Cart> carts = cartDao.findList(true, 100);
			if (CollectionUtils.isNotEmpty(carts)) {
				for (Cart cart : carts) {
					cartDao.remove(cart);
				}
				cartDao.flush();
				cartDao.clear();
			}
			if (carts.size() < 100) {
				break;
			}
		}
	}

}