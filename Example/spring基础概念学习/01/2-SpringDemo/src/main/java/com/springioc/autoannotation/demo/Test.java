package com.springioc.autoannotation.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    @org.junit.Test
    public void testStoreBook()
    {
        //容器
        ApplicationContext ctx=new ClassPathXmlApplicationContext("IOCBeans03.xml");
        BookService bookservice=ctx.getBean(BookService.class);
        bookservice.storeBook("《Spring MVC权威指南 第三版》");
    }
}
