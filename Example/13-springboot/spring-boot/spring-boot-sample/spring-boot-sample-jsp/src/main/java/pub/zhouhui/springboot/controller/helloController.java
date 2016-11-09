package pub.zhouhui.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shane on 2016/10/30.
 */
@RestController
public class helloController {
    @RequestMapping("/getHello")
    public String hello(){
        return"Hello world!";
    }


}
