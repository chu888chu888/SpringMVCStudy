/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entity - 积分记录
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Entity
@Table(name = "xx_point_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_point_log")
public class PointLog extends BaseEntity<Long> {

	private static final long serialVersionUID = -5873966836529051171L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 积分赠送 */
		reward,

		/** 积分兑换 */
		exchange,

		/** 积分兑换撤销 */
		undoExchange,

		/** 积分调整 */
		adjustment
	}

	/** 类型 */
	private PointLog.Type type;

	/** 获取积分 */
	private Long credit;

	/** 扣除积分 */
	private Long debit;

	/** 当前积分 */
	private Long balance;

	/** 操作员 */
	private String operator;

	/** 备注 */
	private String memo;

	/** 会员 */
	private Member member;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@Column(nullable = false, updatable = false)
	public PointLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(PointLog.Type type) {
		this.type = type;
	}

	/**
	 * 获取获取积分
	 * 
	 * @return 获取积分
	 */
	@Column(nullable = false, updatable = false)
	public Long getCredit() {
		return credit;
	}

	/**
	 * 设置获取积分
	 * 
	 * @param credit
	 *            获取积分
	 */
	public void setCredit(Long credit) {
		this.credit = credit;
	}

	/**
	 * 获取扣除积分
	 * 
	 * @return 扣除积分
	 */
	@Column(nullable = false, updatable = false)
	public Long getDebit() {
		return debit;
	}

	/**
	 * 设置扣除积分
	 * 
	 * @param debit
	 *            扣除积分
	 */
	public void setDebit(Long debit) {
		this.debit = debit;
	}

	/**
	 * 获取当前积分
	 * 
	 * @return 当前积分
	 */
	@Column(nullable = false, updatable = false)
	public Long getBalance() {
		return balance;
	}

	/**
	 * 设置当前积分
	 * 
	 * @param balance
	 *            当前积分
	 */
	public void setBalance(Long balance) {
		this.balance = balance;
	}

	/**
	 * 获取操作员
	 * 
	 * @return 操作员
	 */
	@Column(updatable = false)
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	@Column(updatable = false)
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	@Transient
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
	}

}
