package com.chu.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by P70 on 2016/11/21.
 */
@EnableAutoConfiguration
@RestController
public class ApiController {

    @RequestMapping(value = "/api/get/{name}",method = RequestMethod.GET)
    public HashMap<String, Object> get(@PathVariable String name) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("title", "hello world");
        map.put("name", name);
        return map;
    }
}
