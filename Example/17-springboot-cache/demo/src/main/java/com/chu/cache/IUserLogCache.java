package com.chu.cache;

import com.chu.model.UserLog;

/**
 * Created by P70 on 2016/11/25.
 */
public interface IUserLogCache {

    /**
     * 查询
     *
     * @param id
     * @return
     */
    UserLog selectById(Integer id);

    /**
     * 更新
     *
     * @param roncooUserLog
     * @return
     */
    UserLog updateById(UserLog roncooUserLog);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    String deleteById(Integer id);
}
