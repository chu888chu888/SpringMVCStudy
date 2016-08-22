package com.chu.service;

/**
 * Created by chuguangming on 16/8/22.
 */

import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.chu.domain.User;

@ContextConfiguration(locations = {"/applicationContext.xml"})
public class UserServiceTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    private UserService userService;

    @Test
    public void hasMatchUser()
    {
        boolean b1=userService.hasMatchUser("admin","123456");
        boolean b2=userService.hasMatchUser("admin","123456");
        assertTrue(b1);
    }

    @Test
    public void findUserByUserName()
    {
        User user=userService.findUserByUserName("admin");
        assertEquals(user.getUserName(),"admin");
    }
}
