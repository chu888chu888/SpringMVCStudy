/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.shopxx.BaseAttributeConverter;

/**
 * Entity - 发货项
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Entity
@Table(name = "xx_shipping_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_shipping_item")
public class ShippingItem extends BaseEntity<Long> {

	private static final long serialVersionUID = 8266898975871803258L;

	/** 商品编号 */
	private String sn;

	/** 商品名称 */
	private String name;

	/** 数量 */
	private Integer quantity;

	/** 是否需要物流 */
	private Boolean isDelivery;

	/** 商品 */
	private Product product;

	/** 发货单 */
	private Shipping shipping;

	/** 规格 */
	private List<String> specifications = new ArrayList<String>();

	/**
	 * 获取商品编号
	 * 
	 * @return 商品编号
	 */
	@Column(nullable = false, updatable = false)
	public String getSn() {
		return sn;
	}

	/**
	 * 设置商品编号
	 * 
	 * @param sn
	 *            商品编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取商品名称
	 * 
	 * @return 商品名称
	 */
	@Column(nullable = false, updatable = false)
	public String getName() {
		return name;
	}

	/**
	 * 设置商品名称
	 * 
	 * @param name
	 *            商品名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	@NotNull
	@Min(1)
	@Column(nullable = false, updatable = false)
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置数量
	 * 
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取是否需要物流
	 * 
	 * @return 是否需要物流
	 */
	@Column(nullable = false, updatable = false)
	public Boolean getIsDelivery() {
		return isDelivery;
	}

	/**
	 * 设置是否需要物流
	 * 
	 * @param isDelivery
	 *            是否需要物流
	 */
	public void setIsDelivery(Boolean isDelivery) {
		this.isDelivery = isDelivery;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
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
	 * 获取发货单
	 * 
	 * @return 发货单
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Shipping getShipping() {
		return shipping;
	}

	/**
	 * 设置发货单
	 * 
	 * @param shipping
	 *            发货单
	 */
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	/**
	 * 获取规格
	 * 
	 * @return 规格
	 */
	@Column(updatable = false, length = 4000)
	@Convert(converter = SpecificationConverter.class)
	public List<String> getSpecifications() {
		return specifications;
	}

	/**
	 * 设置规格
	 * 
	 * @param specifications
	 *            规格
	 */
	public void setSpecifications(List<String> specifications) {
		this.specifications = specifications;
	}

	/**
	 * 类型转换 - 规格
	 * 
	 * @author SHOP++ Team
	 * @version 4.0
	 */
	@Converter
	public static class SpecificationConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

}
