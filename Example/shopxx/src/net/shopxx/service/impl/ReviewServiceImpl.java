/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.dao.GoodsDao;
import net.shopxx.dao.MemberDao;
import net.shopxx.dao.OrderDao;
import net.shopxx.dao.ReviewDao;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Member;
import net.shopxx.entity.Review;
import net.shopxx.service.ReviewService;
import net.shopxx.util.SystemUtils;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 评论
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("reviewServiceImpl")
public class ReviewServiceImpl extends BaseServiceImpl<Review, Long> implements ReviewService {

	@Resource(name = "reviewDaoImpl")
	private ReviewDao reviewDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;
	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Transactional(readOnly = true)
	public List<Review> findList(Member member, Goods goods, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return reviewDao.findList(member, goods, type, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "review", condition = "#useCache")
	public List<Review> findList(Long memberId, Long goodsId, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		Goods goods = goodsDao.find(goodsId);
		if (goodsId != null && goods == null) {
			return Collections.emptyList();
		}
		return reviewDao.findList(member, goods, type, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Review> findPage(Member member, Goods goods, Review.Type type, Boolean isShow, Pageable pageable) {
		return reviewDao.findPage(member, goods, type, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Goods goods, Review.Type type, Boolean isShow) {
		return reviewDao.count(member, goods, type, isShow);
	}

	@Transactional(readOnly = true)
	public boolean hasPermission(Member member, Goods goods) {
		Assert.notNull(member);
		Assert.notNull(goods);

		Setting setting = SystemUtils.getSetting();
		if (Setting.ReviewAuthority.purchased.equals(setting.getReviewAuthority())) {
			long reviewCount = reviewDao.count(member, goods, null, null);
			long orderCount = orderDao.count(null, net.shopxx.entity.Order.Status.completed, member, goods, null, null, null, null, null, null);
			return orderCount > reviewCount;
		}
		return true;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public Review save(Review review) {
		Assert.notNull(review);

		Review pReview = super.save(review);
		Goods goods = pReview.getGoods();
		if (goods != null) {
			reviewDao.flush();
			long totalScore = reviewDao.calculateTotalScore(goods);
			long scoreCount = reviewDao.calculateScoreCount(goods);
			goods.setTotalScore(totalScore);
			goods.setScoreCount(scoreCount);
			goods.setGenerateMethod(Goods.GenerateMethod.lazy);
		}
		return pReview;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public Review update(Review review) {
		Assert.notNull(review);

		Review pReview = super.update(review);
		Goods goods = pReview.getGoods();
		if (goods != null) {
			reviewDao.flush();
			long totalScore = reviewDao.calculateTotalScore(goods);
			long scoreCount = reviewDao.calculateScoreCount(goods);
			goods.setTotalScore(totalScore);
			goods.setScoreCount(scoreCount);
			goods.setGenerateMethod(Goods.GenerateMethod.lazy);
		}
		return pReview;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public Review update(Review review, String... ignoreProperties) {
		return super.update(review, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public void delete(Review review) {
		if (review != null) {
			super.delete(review);
			Goods goods = review.getGoods();
			if (goods != null) {
				reviewDao.flush();
				long totalScore = reviewDao.calculateTotalScore(goods);
				long scoreCount = reviewDao.calculateScoreCount(goods);
				goods.setTotalScore(totalScore);
				goods.setScoreCount(scoreCount);
				goods.setGenerateMethod(Goods.GenerateMethod.lazy);
			}
		}
	}

}