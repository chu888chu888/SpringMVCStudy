package com.chu.cache;

import com.chu.dao.IUserLogDao;
import com.chu.model.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Created by P70 on 2016/11/25.
 */
@CacheConfig(cacheNames = "roncooCache")
@Repository
public class UserLogCache implements IUserLogCache {
    @Autowired
    private IUserLogDao UserLogDao;

    @Cacheable(key = "#p0")
    @Override
    public UserLog selectById(Integer id) {
        System.out.println("查询功能，缓存找不到，直接读库, id=" + id);
        return UserLogDao.findOne(id);
    }

    @CachePut(key = "#p0")
    @Override
    public UserLog updateById(UserLog roncooUserLog) {
        System.out.println("更新功能，更新缓存，直接写库, id=" + roncooUserLog);
        return UserLogDao.save(roncooUserLog);
    }

    @CacheEvict(key = "#p0")
    @Override
    public String deleteById(Integer id) {
        System.out.println("删除功能，删除缓存，直接写库, id=" + id);
        return "清空缓存成功";
    }
}
