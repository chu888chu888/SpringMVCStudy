package pub.zhouhui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import pub.zhouhui.domin.User;
import pub.zhouhui.domin.UserRepository;

/**
 * Created by shane on 2016/10/31.
 */
@Controller
public class UserController {
    @Resource
    UserRepository userRepository;

    @RequestMapping("/userLogin")
    @ResponseBody
    public String login(String userName) {
        User ul = userRepository.findByName(userName);
        if (ul == null) {
            return "Login Error";
        } else {
            System.out.println(ul.getName() + " " + ul.getAge().longValue());
        }
        //return result;
        return "login succerr!!!";
    }
}
