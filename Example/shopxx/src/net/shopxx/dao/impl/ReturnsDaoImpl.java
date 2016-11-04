/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.dao.impl;

import net.shopxx.dao.ReturnsDao;
import net.shopxx.entity.Returns;

import org.springframework.stereotype.Repository;

/**
 * Dao - 退货单
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Repository("returnsDaoImpl")
public class ReturnsDaoImpl extends BaseDaoImpl<Returns, Long> implements ReturnsDao {

}