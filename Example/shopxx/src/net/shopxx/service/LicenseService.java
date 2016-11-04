/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service;

/**
 * Service - 许可
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public interface LicenseService {

	/**
	 * 获取许可信息
	 * 
	 * @param sn
	 *            序列号
	 * @param siteUrl
	 *            网站网址
	 * @return 许可信息
	 */
	byte[] getLicense(String sn, String siteUrl);

}