package com.proxy;

import com.springioc.annotation.demo.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chuguangming on 16/8/19.
 */
public class Test {

    @org.junit.Test
    public void test01()
    {
        Math math=new Math();
        int n1=100,n2=5;
        math.add(n1, n2);
        math.sub(n1, n2);
        math.mut(n1, n2);
        math.div(n1, n2);
    }

    @org.junit.Test
    public void test02()
    {
        Math2 math=new Math2();
        int n1=100,n2=5;
        math.add(n1, n2);
        math.sub(n1, n2);
        math.mut(n1, n2);
        math.div(n1, n2);
    }
}
