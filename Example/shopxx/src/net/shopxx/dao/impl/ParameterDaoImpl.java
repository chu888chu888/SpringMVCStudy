/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.dao.impl;

import net.shopxx.dao.ParameterDao;
import net.shopxx.entity.Parameter;

import org.springframework.stereotype.Repository;

/**
 * Dao - 参数
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Repository("parameterDaoImpl")
public class ParameterDaoImpl extends BaseDaoImpl<Parameter, Long> implements ParameterDao {

}