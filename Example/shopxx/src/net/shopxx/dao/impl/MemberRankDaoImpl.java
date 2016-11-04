/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.dao.impl;

import java.math.BigDecimal;

import javax.persistence.NoResultException;

import net.shopxx.dao.MemberRankDao;
import net.shopxx.entity.MemberRank;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 会员等级
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Repository("memberRankDaoImpl")
public class MemberRankDaoImpl extends BaseDaoImpl<MemberRank, Long> implements MemberRankDao {

	public boolean nameExists(String name) {
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		String jpql = "select count(*) from MemberRank memberRank where lower(memberRank.name) = lower(:name)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("name", name).getSingleResult();
		return count > 0;
	}

	public boolean amountExists(BigDecimal amount) {
		if (amount == null) {
			return false;
		}
		String jpql = "select count(*) from MemberRank memberRank where memberRank.amount = :amount";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("amount", amount).getSingleResult();
		return count > 0;
	}

	public MemberRank findDefault() {
		try {
			String jpql = "select memberRank from MemberRank memberRank where memberRank.isDefault = true";
			return entityManager.createQuery(jpql, MemberRank.class).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public MemberRank findByAmount(BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		String jpql = "select memberRank from MemberRank memberRank where memberRank.isSpecial = false and memberRank.amount <= :amount order by memberRank.amount desc";
		return entityManager.createQuery(jpql, MemberRank.class).setParameter("amount", amount).setMaxResults(1).getSingleResult();
	}

	public void setDefault(MemberRank memberRank) {
		Assert.notNull(memberRank);

		memberRank.setIsDefault(true);
		if (memberRank.isNew()) {
			String jpql = "update MemberRank memberRank set memberRank.isDefault = false where memberRank.isDefault = true";
			entityManager.createQuery(jpql).executeUpdate();
		} else {
			String jpql = "update MemberRank memberRank set memberRank.isDefault = false where memberRank.isDefault = true and memberRank != :memberRank";
			entityManager.createQuery(jpql).setParameter("memberRank", memberRank).executeUpdate();
		}
	}

}