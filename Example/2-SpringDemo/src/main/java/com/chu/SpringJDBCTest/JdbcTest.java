package com.chu.SpringJDBCTest;

/**
 * Created by chuguangming on 2016/10/27.
 */
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.*;

import com.mysql.jdbc.*;

public class JdbcTest {
    public static void main(String [] args)
    {
        DriverManagerDataSource driverManagerDataSource=new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("");

        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(driverManagerDataSource);

        String sql="create table ttt2(user_id int primary key)";
        jdbcTemplate.execute(sql);

    }
}
