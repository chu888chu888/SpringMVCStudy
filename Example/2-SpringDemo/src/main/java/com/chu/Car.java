package com.chu;

/**
 * Created by chuguangming on 16/8/18.
 */
public class Car implements Driveable {

    @Override
    public void drive() {
        System.out.println("小汽车被开着跑了");
    }

    private String Brand;
    private int MaxSpeed;

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getMaxSpeed() {
        return MaxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        MaxSpeed = maxSpeed;
    }
}
