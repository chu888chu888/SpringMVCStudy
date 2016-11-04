/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service;

import java.util.List;
import java.util.Map;

import net.shopxx.entity.Shipping;

/**
 * Service - 发货单
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public interface ShippingService extends BaseService<Shipping, Long> {

	/**
	 * 根据编号查找发货单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 发货单，若不存在则返回null
	 */
	Shipping findBySn(String sn);

	/**
	 * 获取物流动态
	 * 
	 * @param shipping
	 *            发货单
	 * @return 物流动态
	 */
	List<Map<String, String>> getTransitSteps(Shipping shipping);

}