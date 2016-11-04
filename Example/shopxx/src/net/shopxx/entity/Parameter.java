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
import javax.validation.constraints.NotNull;

import net.shopxx.BaseAttributeConverter;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 参数
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Entity
@Table(name = "xx_parameter")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_parameter")
public class Parameter extends OrderEntity<Long> {

	private static final long serialVersionUID = -6571180357881415029L;

	/** 参数组 */
	private String group;

	/** 绑定分类 */
	private ProductCategory productCategory;

	/** 参数名称 */
	private List<String> names = new ArrayList<String>();

	/**
	 * 获取参数组
	 * 
	 * @return 参数组
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(name = "parameter_group", nullable = false)
	public String getGroup() {
		return group;
	}

	/**
	 * 设置参数组
	 * 
	 * @param group
	 *            参数组
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * 获取绑定分类
	 * 
	 * @return 绑定分类
	 */
	@NotNull(groups = Save.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * 设置绑定分类
	 * 
	 * @param productCategory
	 *            绑定分类
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * 获取参数名称
	 * 
	 * @return 参数名称
	 */
	@NotEmpty
	@Column(nullable = false, length = 4000)
	@Convert(converter = NameConverter.class)
	public List<String> getNames() {
		return names;
	}

	/**
	 * 设置参数名称
	 * 
	 * @param names
	 *            参数名称
	 */
	public void setNames(List<String> names) {
		this.names = names;
	}

	/**
	 * 类型转换 - 参数名称
	 * 
	 * @author SHOP++ Team
	 * @version 4.0
	 */
	@Converter
	public static class NameConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

}
