/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import javax.annotation.Resource;

import net.shopxx.dao.LogDao;
import net.shopxx.entity.Log;
import net.shopxx.service.LogService;

import org.springframework.stereotype.Service;

/**
 * Service - 日志
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("logServiceImpl")
public class LogServiceImpl extends BaseServiceImpl<Log, Long> implements LogService {

	@Resource(name = "logDaoImpl")
	private LogDao logDao;

	public void clear() {
		logDao.removeAll();
	}

}