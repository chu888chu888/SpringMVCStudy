/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.job;

import javax.annotation.Resource;

import net.shopxx.service.OrderService;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job - 订单
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Lazy(false)
@Component("orderJob")
public class OrderJob {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	/**
	 * 过期订单处理
	 */
	@Scheduled(cron = "${job.order_expired_processing.cron}")
	public void expiredProcessing() {
		orderService.undoExpiredUseCouponCode();
		orderService.undoExpiredExchangePoint();
		orderService.releaseExpiredAllocatedStock();
	}

}