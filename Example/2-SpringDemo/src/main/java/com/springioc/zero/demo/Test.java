package com.springioc.zero.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    @org.junit.Test
    public void testStoreBook()
    {
        //容器，注解配置应用程序容器，Spring通过反射ApplicationCfg.class初始化容器
        ApplicationContext ctx=new AnnotationConfigApplicationContext(ApplicationCfg.class);
        BookService bookservice=ctx.getBean(BookService.class);
        bookservice.storeBook("《Spring MVC权威指南 第四版》");
        User user1=ctx.getBean("user1",User.class);
        user1.show();
        User getUser=ctx.getBean("getUser",User.class);
        getUser.show();
    }
}
