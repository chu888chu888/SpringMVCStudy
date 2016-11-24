package com.chu;

import com.chu.component.RedisComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by P70 on 2016/11/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DempApplicationTestRedis {
    @Autowired
    private RedisComponent RedisComponent;

    @Test
    public void set() {
        RedisComponent.set("chu", "楚广明");
    }

    @Test
    public void get() {
        System.out.println(RedisComponent.get("chu"));
    }

    @Test
    public void del() {
        RedisComponent.del("chu");
    }
}
