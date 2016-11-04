/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.dao;

import net.shopxx.entity.DeliveryCenter;

/**
 * Dao - 发货点
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public interface DeliveryCenterDao extends BaseDao<DeliveryCenter, Long> {

	/**
	 * 查找默认发货点
	 * 
	 * @return 默认发货点，若不存在则返回null
	 */
	DeliveryCenter findDefault();

	/**
	 * 设置默认发货点
	 * 
	 * @param deliveryCenter
	 *            发货点
	 */
	void setDefault(DeliveryCenter deliveryCenter);

}