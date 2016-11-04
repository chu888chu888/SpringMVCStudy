/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.dao.impl;

import net.shopxx.dao.StockLogDao;
import net.shopxx.entity.StockLog;

import org.springframework.stereotype.Repository;

/**
 * Dao - 库存记录
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Repository("stockLogDaoImpl")
public class StockLogDaoImpl extends BaseDaoImpl<StockLog, Long> implements StockLogDao {

}