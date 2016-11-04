/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.dao;

import java.util.List;
import java.util.Set;

import net.shopxx.entity.Goods;
import net.shopxx.entity.Product;

/**
 * Dao - 商品
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public interface ProductDao extends BaseDao<Product, Long> {

	/**
	 * 判断编号是否存在
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 编号是否存在
	 */
	boolean snExists(String sn);

	/**
	 * 根据编号查找商品
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 商品，若不存在则返回null
	 */
	Product findBySn(String sn);

	/**
	 * 通过编号、名称查找商品
	 * 
	 * @param type
	 *            类型
	 * @param keyword
	 *            关键词
	 * @param excludes
	 *            排除商品
	 * @param count
	 *            数量
	 * @return 商品
	 */
	List<Product> search(Goods.Type type, String keyword, Set<Product> excludes, Integer count);

}