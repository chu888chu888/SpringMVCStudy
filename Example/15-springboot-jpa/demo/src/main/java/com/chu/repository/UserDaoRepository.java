package com.chu.repository;

/**
 * Created by P70 on 2016/11/22.
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import com.chu.dao.IUserDao;
import com.chu.model.User;
import com.chu.util.base.JdbcDaoImplement;
import com.chu.util.base.Page;
import com.chu.util.base.Sql;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoRepository extends JdbcDaoImplement implements IUserDao {
    @Override
    public int insert(User user) {
        String sql = "insert into User (name, create_time) values (?, ?)";
        return jdbcTemplate.update(sql, user.getName(), user.getDate());
    }

    @Override
    public int deleteById(int id) {
        String sql = "delete from User where id=?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int updateById(User user) {
        String sql = "update User set name=?, create_time=? where id=?";
        return jdbcTemplate.update(sql, user.getName(), user.getDate(), user.getId());
    }

    @Override
    public User selectById(int id) {
        String sql = "select * from User where id=?";

        return queryForObject(sql, User.class, id);
    }

    @Override
    public Page<User> queryForPage(int pageCurrent, int pageSize, String name) {


        // 若要like查询，如下
        StringBuffer sql = new StringBuffer("select * from User where 1");
        // Sql.checkSql 的作用是防止sql注入
        sql.append(" and name like '%").append(Sql.checkSql(name)).append("%' ");
        return queryForPage(sql.toString(), pageCurrent, pageSize, User.class);
    }

}
