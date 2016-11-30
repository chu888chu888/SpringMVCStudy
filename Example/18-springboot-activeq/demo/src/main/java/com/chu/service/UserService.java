package com.chu.service;

import com.chu.dao.IUserLogDao;
import com.chu.model.User;
import com.chu.model.UserLog;
import com.chu.repository.UserDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by P70 on 2016/11/23.
 */
@Service
public class UserService {
    @Autowired
    private UserDaoRepository userDaoRepository;

    @Autowired
    private IUserLogDao userLogDao;

    /**
     * 用户注册
     *
     * @return
     */
    @Transactional
    public String register(String name, String ip) {
        // 1.添加用户
        User user = new User();
        user.setName(name);
        user.setDate(new Date());
        userDaoRepository.insert(user);

        // 测试使用
        /*
        boolean flag = true;
        if (flag) {
            throw new RuntimeException();
        }
        */

        // 2.添加注册日志
        UserLog roncooUserLog = new UserLog();
        roncooUserLog.setUserName(name);
        roncooUserLog.setUserIp(ip);
        roncooUserLog.setCreateTime(new Date());
        userLogDao.save(roncooUserLog);

        return "success";
    }

}
