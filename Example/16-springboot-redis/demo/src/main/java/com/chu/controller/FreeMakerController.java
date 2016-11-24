package com.chu.controller;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by P70 on 2016/11/17.
 */

@EnableAutoConfiguration
@Controller
@RequestMapping(value = "/FreeMaker", method = RequestMethod.GET)
public class FreeMakerController {

    private static final Logger logger= LoggerFactory.getLogger(FreeMakerController.class);


    @RequestMapping("index")
    public String index(ModelMap map)
    {
        logger.info("这是一个测试模板");
        map.put("title","FreeMarker模板");
        return "index";
    }

}
