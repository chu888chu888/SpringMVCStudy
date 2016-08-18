package com.chu;

/**
 * Created by chuguangming on 16/8/18.
 */
public class Test {
    public static void main(String[] args) {
        Person person=new Person();
        person.setName("张三");
        person.setDriveable(new Car());
        person.drive();
    }
}
