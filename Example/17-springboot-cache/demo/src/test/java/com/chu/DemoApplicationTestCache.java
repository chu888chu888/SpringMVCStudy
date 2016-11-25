package com.chu;

import com.chu.cache.UserLogCache;
import com.chu.model.UserLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by P70 on 2016/11/25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTestCache {

    @Autowired
    private UserLogCache UserLogCache;

    @Test
    public void insert() {
        UserLog bean = UserLogCache.selectById(1);
        bean.setUserName("测试");
        bean.setCreateTime(new Date());
        UserLogCache.updateById(bean);
        UserLogCache.deleteById(1);

    }

}
