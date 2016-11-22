package com.chu.dao;

import com.chu.model.User;
import com.chu.util.base.Page;

/**
 * Created by P70 on 2016/11/22.
 */
public interface IUserDao {

    /**
     * 插入
     *
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int deleteById(int id);

    /**
     * 更新
     *
     * @param user
     * @return
     */
    int updateById(User user);

    /**
     * 查找
     *
     * @param id
     * @return
     */
    User selectById(int id);

    /**
     * @return
     */

    Page<User> queryForPage(int pageCurrent, int pageSize, String name);
}
