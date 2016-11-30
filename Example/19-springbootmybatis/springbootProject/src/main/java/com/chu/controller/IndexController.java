package com.chu.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by P70 on 2016/11/10.
 */

@RestController
@EnableAutoConfiguration
public class IndexController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "hello world!";
    }


}
