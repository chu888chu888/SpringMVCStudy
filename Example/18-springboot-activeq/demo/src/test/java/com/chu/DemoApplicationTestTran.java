package com.chu;

import com.chu.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by P70 on 2016/11/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTestTran {
    @Autowired
    private UserService userService;

    @Test
    public void register() {
        String result = userService.register("无境ff", "192.168.1.1");
        System.out.println(result);
    }
}
