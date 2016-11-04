/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx;

import java.math.BigDecimal;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.builtin.NumericFieldBridge;

/**
 * BigDecimal类型转换
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public class BigDecimalNumericFieldBridge extends NumericFieldBridge {

	/**
	 * 获取BigDecimal对象
	 * 
	 * @param name
	 *            名称
	 * @param document
	 *            Document
	 * @return BigDecimal对象
	 */
	public Object get(String name, Document document) {
		return new BigDecimal(document.getFieldable(name).stringValue());
	}

	/**
	 * 设置BigDecimal对象
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 * @param document
	 *            Document
	 * @param luceneOptions
	 *            LuceneOptions
	 */
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		if (value != null) {
			BigDecimal decimalValue = (BigDecimal) value;
			luceneOptions.addNumericFieldToDocument(name, decimalValue.doubleValue(), document);
		}
	}

}