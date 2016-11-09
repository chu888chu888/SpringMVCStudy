package com.chu.controller;

/**
 * Created by P70 on 2016/11/9.
 */
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@EnableAutoConfiguration
public class AdminController {
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String index(){
        return "admin/index";//意思是：调用admin文件夹下的index.html模板
    }

}