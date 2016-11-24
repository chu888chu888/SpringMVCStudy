/**
 * 2015-2016 龙果学院 (www.roncoo.com)
 */
package com.roncoo.education.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author wujing
 */
@Entity
public class RoncooUserLog {

	@Id
	@GeneratedValue
	private Integer id;

	@Column
	private Date createTime;

	@Column
	private String userName;

	@Column
	private String userIp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

}
