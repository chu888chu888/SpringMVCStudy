/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service;

import net.shopxx.entity.Cart;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;

/**
 * Service - 购物车
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public interface CartService extends BaseService<Cart, Long> {

	/**
	 * 获取当前购物车
	 * 
	 * @return 当前购物车，若不存在则返回null
	 */
	Cart getCurrent();

	/**
	 * 添加商品至当前购物车
	 * 
	 * @param product
	 *            商品
	 * @param quantity
	 *            数量
	 * @return 当前购物车
	 */
	Cart add(Product product, int quantity);

	/**
	 * 合并临时购物车至会员
	 * 
	 * @param member
	 *            会员
	 * @param cart
	 *            临时购物车
	 */
	void merge(Member member, Cart cart);

	/**
	 * 清除过期购物车
	 */
	void evictExpired();

}