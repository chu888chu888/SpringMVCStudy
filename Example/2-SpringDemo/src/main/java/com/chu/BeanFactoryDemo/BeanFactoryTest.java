package com.chu.BeanFactoryDemo;

/**
 * Created by chuguangming on 2016/10/26.
 */
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.chu.Car;

public class BeanFactoryTest {
    public static void main(String [] args) throws Throwable
    {
        ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        Resource resource=resolver.getResource("classpath:com/chu/BeanFactoryDemo/beans.xml");

        BeanFactory beanFactory=new XmlBeanFactory(resource);
        System.out.println("init BeanFactory...");
        Car car=beanFactory.getBean("car1",Car.class);
        System.out.println("car bean is ready for use!");


        ApplicationContext ctx=new AnnotationConfigApplicationContext(Beans.class);
        Car car2=ctx.getBean("car",Car.class);

    }
}
