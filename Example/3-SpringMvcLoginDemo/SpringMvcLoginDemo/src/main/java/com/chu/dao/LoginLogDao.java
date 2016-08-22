package com.chu.dao;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.chu.domain.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

/**
 * Created by chuguangming on 16/8/22.
 */
@Repository
public class LoginLogDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertLoginLog(LoginLog loginLog)
    {
        String sqlstr="INSERT INTO t_login_log(user_id,ip,login_datetime) "
                +"Values(?,?,?)";
        Object [] args={loginLog.getUserId(),loginLog.getIp(),loginLog.getLoginDate()};
        jdbcTemplate.update(sqlstr,args);

    }

}
