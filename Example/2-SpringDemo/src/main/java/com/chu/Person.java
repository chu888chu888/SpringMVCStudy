package com.chu;

/**
 * Created by chuguangming on 16/8/18.
 */
public class Person {
    String name;
    Driveable driveable;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Driveable getDriveable() {
        return driveable;
    }
    public void setDriveable(Driveable driveable) {
        this.driveable = driveable;
    }


    public void drive(){
        System.out.println(this.name+"准备开车");
        this.driveable.drive();
    }
}
