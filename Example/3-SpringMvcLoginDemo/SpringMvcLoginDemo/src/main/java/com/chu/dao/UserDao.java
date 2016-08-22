package com.chu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

/**
 * Created by chuguangming on 16/8/19.
 */

import com.chu.domain.User;


@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getMatchCount(String userName,String password)
    {
        String sqlStr="select count(*) from t_user "
                +" where user_name=? and password=?";
        return jdbcTemplate.queryForObject(sqlStr,new Object[]{userName,password}, Integer.class);

    }
    public void updateLoginInfo(User user)
    {
        String sqlStr="Update t_user SET last_visit=?,last_ip=? "
                +" where user_id=? ";

        jdbcTemplate.update(sqlStr,new Object[] {user.getLastVisit(),user.getLastIp(),user.getUserID()});

    }

    public User findUserByUserName(final String userName)
    {
        String sqlStr="select user_id,user_name from t_user where user_name=?";
        final User user=new User();
        jdbcTemplate.query(sqlStr, new Object[]{userName},
                new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        user.setUserID(resultSet.getInt("user_id"));
                        user.setUserName(userName);
                    }
                });
        return user;
    }

}
