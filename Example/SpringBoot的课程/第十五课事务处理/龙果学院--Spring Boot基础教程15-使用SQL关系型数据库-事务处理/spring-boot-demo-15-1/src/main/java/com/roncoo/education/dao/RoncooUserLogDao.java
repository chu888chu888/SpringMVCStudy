/**
 * 2015-2016 龙果学院 (www.roncoo.com)
 */
package com.roncoo.education.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roncoo.education.bean.RoncooUserLog;

/**
 * @author wujing
 */
public interface RoncooUserLogDao extends JpaRepository<RoncooUserLog, Integer> {

}
