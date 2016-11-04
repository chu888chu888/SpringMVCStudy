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
 * Entity - 库存记录
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Entity
@Table(name = "xx_stock_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_stock_log")
public class StockLog extends BaseEntity<Long> {

	private static final long serialVersionUID = 5041320926905972377L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 入库 */
		stockIn,

		/** 出库 */
		stockOut
	}

	/** 类型 */
	private StockLog.Type type;

	/** 入库数量 */
	private Integer inQuantity;

	/** 出库数量 */
	private Integer outQuantity;

	/** 当前库存 */
	private Integer stock;

	/** 操作员 */
	private String operator;

	/** 备注 */
	private String memo;

	/** 商品 */
	private Product product;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@Column(nullable = false, updatable = false)
	public StockLog.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(StockLog.Type type) {
		this.type = type;
	}

	/**
	 * 获取入库数量
	 * 
	 * @return 入库数量
	 */
	@Column(nullable = false, updatable = false)
	public Integer getInQuantity() {
		return inQuantity;
	}

	/**
	 * 设置入库数量
	 * 
	 * @param inQuantity
	 *            入库数量
	 */
	public void setInQuantity(Integer inQuantity) {
		this.inQuantity = inQuantity;
	}

	/**
	 * 获取出库数量
	 * 
	 * @return 出库数量
	 */
	@Column(nullable = false, updatable = false)
	public Integer getOutQuantity() {
		return outQuantity;
	}

	/**
	 * 设置出库数量
	 * 
	 * @param outQuantity
	 *            出库数量
	 */
	public void setOutQuantity(Integer outQuantity) {
		this.outQuantity = outQuantity;
	}

	/**
	 * 获取当前库存
	 * 
	 * @return 当前库存
	 */
	@Column(nullable = false, updatable = false)
	public Integer getStock() {
		return stock;
	}

	/**
	 * 设置当前库存
	 * 
	 * @param stock
	 *            当前库存
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * 获取操作员
	 * 
	 * @return 操作员
	 */
	@Column(nullable = false, updatable = false)
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
	 * 获取商品
	 * 
	 * @return 商品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * 
	 * @param product
	 *            商品
	 */
	public void setProduct(Product product) {
		this.product = product;
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
