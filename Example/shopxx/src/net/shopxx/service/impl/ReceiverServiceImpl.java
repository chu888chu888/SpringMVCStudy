/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import javax.annotation.Resource;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.ReceiverDao;
import net.shopxx.entity.Member;
import net.shopxx.entity.Receiver;
import net.shopxx.service.ReceiverService;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 收货地址
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("receiverServiceImpl")
public class ReceiverServiceImpl extends BaseServiceImpl<Receiver, Long> implements ReceiverService {

	@Resource(name = "receiverDaoImpl")
	private ReceiverDao receiverDao;

	@Transactional(readOnly = true)
	public Receiver findDefault(Member member) {
		return receiverDao.findDefault(member);
	}

	@Transactional(readOnly = true)
	public Page<Receiver> findPage(Member member, Pageable pageable) {
		return receiverDao.findPage(member, pageable);
	}

	@Override
	@Transactional
	public Receiver save(Receiver receiver) {
		Assert.notNull(receiver);

		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			receiverDao.setDefault(receiver);
		}
		return super.save(receiver);
	}

	@Override
	@Transactional
	public Receiver update(Receiver receiver) {
		Assert.notNull(receiver);

		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			receiverDao.setDefault(receiver);
		}
		return super.update(receiver);
	}

}