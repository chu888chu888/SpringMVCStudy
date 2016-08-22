package com.chu.service;

/**
 * Created by chuguangming on 16/8/22.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chu.dao.*;
import com.chu.domain.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginLogDao loginLogDao;

    public boolean hasMatchUser(String userName,String password)
    {
        int matchCount=userDao.getMatchCount(userName,password);
        return matchCount>0;
    }
    public User findUserByUserName(String userName)
    {
        return userDao.findUserByUserName(userName);
    }
    public void loginSuccess(User user)
    {
        LoginLog loginLog=new LoginLog();
        loginLog.setUserId(user.getUserID());
        loginLog.setIp(user.getLastIp());
        loginLog.setLoginDate(user.getLastVisit());
        loginLogDao.insertLoginLog(loginLog);
    }
}
