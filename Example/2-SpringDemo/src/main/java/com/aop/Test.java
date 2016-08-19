package com.aop;

/**
 * Created by chuguangming on 16/8/19.
 */
import com.springioc.annotation.demo.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Test {
    @org.junit.Test
    public void testStoreBook()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("aop01.xml");
        Math math = ctx.getBean("math", Math.class);
        int n1 = 100, n2 = 5;
        math.add(n1, n2);
        math.sub(n1, n2);
        math.mut(n1, n2);
        math.div(n1, n2);
    }
}