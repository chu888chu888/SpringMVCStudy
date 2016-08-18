
package com.springioc.chu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by chuguangming on 16/8/18.
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/ioctest.xml");
        Person person=(Person) context.getBean("person");
        person.drive();

    }
}
