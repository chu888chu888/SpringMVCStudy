package pub.zhouhui.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pub.zhouhui.springboot.bean.Demo;

/**
 * Created by shane on 2016/10/30.
 */
@RestController
public class DemoController {
    @RequestMapping("/getJson")
    public Demo getDemo() {
        Demo demo = new Demo();
        demo.setId(1);
        demo.setName("longwanjun");
        return demo;
    }

    @RequestMapping("/zeroException")
    public int zeroException() {
        return 100 / 0;
    }
}
