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
 * Entity - 订单记录
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Entity
@Table(name = "xx_order_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order_log")
public class OrderLog extends BaseEntity<Long> {

	private static final long serialVersionUID = -3271116283622540763L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 订单创建 */
		create,

		/** 订单更新 */
		update,

		/** 订单取消 */
		cancel,

		/** 订单审核 */
		review,

		/** 订单收款 */
		payment,

		/** 订单退款 */
		refunds,

		/** 订单发货 */
		shipping,

		/** 订单退货 */
		returns,

		/** 订单收货 */
		receive,

		/** 订单完成 */
		complete,

		/** 订单失败 */
		fail
	}

	/** 类型 */
	private OrderLog.Type type;

	/** 操作员 */
	private String operator;

	/** 内容 */
	private String content;

	/** 订单 */
	private Order order;

	/**
	 * 构造方法
	 */
	public OrderLog() {
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param operator
	 *            操作员
	 */
	public OrderLog(OrderLog.Type type, String operator) {
		this.type = type;
		this.operator = operator;
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param operator
	 *            操作员
	 * @param content
	 *            内容
	 */
	public OrderLog(OrderLog.Type type, String operator, String content) {
		this.type = type;
		this.operator = operator;
		this.content = content;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@Column(nullable = false, updatable = false)
	public OrderLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(OrderLog.Type type) {
		this.type = type;
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
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@Column(updatable = false)
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
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
