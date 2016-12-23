package com.springioc.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 图书业务类
 */
public class BookService {
    IBookDAO bookDAO;

    public BookService() {
        //容器
        ApplicationContext ctx=new ClassPathXmlApplicationContext("IOCBeans01.xml");
        //从容器中获得id为bookdao的bean
        bookDAO=(IBookDAO)ctx.getBean("bookdao");
    }

    public void storeBook(String bookname){
        System.out.println("图书上货");
        String result=bookDAO.addBook(bookname);
        System.out.println(result);
    }
}
