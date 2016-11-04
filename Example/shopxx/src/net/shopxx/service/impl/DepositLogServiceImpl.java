/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import javax.annotation.Resource;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.DepositLogDao;
import net.shopxx.entity.DepositLog;
import net.shopxx.entity.Member;
import net.shopxx.service.DepositLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 预存款记录
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("depositLogServiceImpl")
public class DepositLogServiceImpl extends BaseServiceImpl<DepositLog, Long> implements DepositLogService {

	@Resource(name = "depositLogDaoImpl")
	private DepositLogDao depositLogDao;

	@Transactional(readOnly = true)
	public Page<DepositLog> findPage(Member member, Pageable pageable) {
		return depositLogDao.findPage(member, pageable);
	}

}