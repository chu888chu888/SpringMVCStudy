/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.dao.impl;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.dao.CouponCodeDao;
import net.shopxx.entity.Coupon;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Member;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Dao - 优惠码
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Repository("couponCodeDaoImpl")
public class CouponCodeDaoImpl extends BaseDaoImpl<CouponCode, Long> implements CouponCodeDao {

	public boolean codeExists(String code) {
		if (StringUtils.isEmpty(code)) {
			return false;
		}
		String jpql = "select count(*) from CouponCode couponCode where lower(couponCode.code) = lower(:code)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("code", code).getSingleResult();
		return count > 0;
	}

	public CouponCode findByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		try {
			String jpql = "select couponCode from CouponCode couponCode where lower(couponCode.code) = lower(:code)";
			return entityManager.createQuery(jpql, CouponCode.class).setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Page<CouponCode> findPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		if (member != null) {
			criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		}
		return super.findPage(criteriaQuery, pageable);
	}

	public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		Path<Coupon> couponPath = root.get("coupon");
		if (coupon != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(couponPath, coupon));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (hasBegun != null) {
			if (hasBegun) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(couponPath.get("beginDate").isNull(), criteriaBuilder.lessThanOrEqualTo(couponPath.<Date> get("beginDate"), new Date())));
			} else {
				restrictions = criteriaBuilder.and(restrictions, couponPath.get("beginDate").isNotNull(), criteriaBuilder.greaterThan(couponPath.<Date> get("beginDate"), new Date()));
			}
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, couponPath.get("endDate").isNotNull(), criteriaBuilder.lessThanOrEqualTo(couponPath.<Date> get("endDate"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(couponPath.get("endDate").isNull(), criteriaBuilder.greaterThan(couponPath.<Date> get("endDate"), new Date())));
			}
		}
		if (isUsed != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUsed"), isUsed));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

}