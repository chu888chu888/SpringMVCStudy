/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 规格值
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public class SpecificationValue implements Serializable {

	private static final long serialVersionUID = 4151307834993215662L;

	/** ID */
	private Integer id;

	/** 值 */
	private String value;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	@NotNull
	public Integer getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 */
	@NotEmpty
	@Length(max = 200)
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 *            值
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
