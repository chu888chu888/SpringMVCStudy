package com.chu.web;

/**
 * Created by chuguangming on 16/8/22.
 */

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.chu.domain.*;
import com.chu.service.UserService;


@Controller
@RequestMapping(value = "/admin")
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login.html")
    public String loginPage()
    {
        return "login";
    }

    @RequestMapping(value = "/loginCheck.html")
    public ModelAndView loginCheck(HttpServletRequest request,LoginCommand loginCommand)
    {
        boolean isValidUser=userService.hasMatchUser(loginCommand.getUserName(),loginCommand.getPassWord());
        if (!isValidUser)
        {
            return new ModelAndView("login","error","用户名与密码错误");
        }
        else
        {
            User user=userService.findUserByUserName(loginCommand.getUserName());
            user.setLastIp(request.getRemoteAddr());
            user.setLastVisit(new Date());
            userService.loginSuccess(user);
            request.getSession().setAttribute("user",user);
            return new ModelAndView("main");

        }
    }
}
