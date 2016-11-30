package com.chu.mapper;

/**
 * Created by P70 on 2016/11/30.
 */
import com.chu.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface UserMapper {
    @Insert(value = "insert into user (name, create_time) values (#{name,jdbcType=VARCHAR}, #{date,jdbcType=TIMESTAMP})")
    int insert(User record);

    @Select(value = "select id, name, create_time from user where id = #{id,jdbcType=INTEGER}")
    @Results(value = { @Result(column = "date", property = "date", jdbcType = JdbcType.TIMESTAMP) })
    User selectByPrimaryKey(Integer id);
}
