package com.springioc.demo;

/**
 * Created by chuguangming on 16/8/18.
 */
public class Test {
    @org.junit.Test
    public void testStoreBook()
    {
        BookService bookservice=new BookService();
        bookservice.storeBook("《Spring MVC权威指南 第一版》");
    }
}
