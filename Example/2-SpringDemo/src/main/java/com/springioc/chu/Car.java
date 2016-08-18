package com.springioc.chu;

/**
 * Created by chuguangming on 16/8/18.
 */
public class Car implements Driveable {

    @Override
    public void drive() {
        System.out.println("小汽车被开着跑了");
    }
}
