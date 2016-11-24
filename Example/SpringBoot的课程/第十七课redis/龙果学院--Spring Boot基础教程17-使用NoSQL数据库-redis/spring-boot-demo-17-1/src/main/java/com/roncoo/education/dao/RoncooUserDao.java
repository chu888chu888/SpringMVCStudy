/**
 * 2015-2016 龙果学院 (www.roncoo.com)
 */
package com.roncoo.education.dao;

import com.roncoo.education.bean.RoncooUser;
import com.roncoo.education.util.base.Page;

/**
 * 接口
 * 
 * @author wujing
 */
public interface RoncooUserDao {

	int insert(RoncooUser roncooUser);

	int deleteById(int id);

	int updateById(RoncooUser roncooUser);

	RoncooUser selectById(int id);

	Page<RoncooUser> queryForPage(int i, int j, String string);

}
