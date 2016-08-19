package com.proxy;

/**
 * Created by chuguangming on 16/8/19.
 */
import java.util.Random;

public class Math2 {
    //加
    public int add(int n1, int n2) {
        //开始时间
        long start = System.currentTimeMillis();
        lazy();
        int result = n1 + n2;
        System.out.println(n1 + "+" + n2 + "=" + result);
        Long span = System.currentTimeMillis() - start;
        System.out.println("共用时：" + span);
        return result;
    }

    //减
    public int sub(int n1, int n2) {
        //开始时间
        long start = System.currentTimeMillis();
        lazy();
        int result = n1 - n2;
        System.out.println(n1 + "-" + n2 + "=" + result);
        Long span = System.currentTimeMillis() - start;
        System.out.println("共用时：" + span);
        return result;
    }

    //乘
    public int mut(int n1, int n2) {
        //开始时间
        long start = System.currentTimeMillis();
        lazy();
        int result = n1 * n2;
        System.out.println(n1 + "X" + n2 + "=" + result);
        Long span = System.currentTimeMillis() - start;
        System.out.println("共用时：" + span);
        return result;
    }

    //除
    public int div(int n1, int n2) {
        //开始时间
        long start = System.currentTimeMillis();
        lazy();
        int result = n1 / n2;
        System.out.println(n1 + "/" + n2 + "=" + result);
        Long span = System.currentTimeMillis() - start;
        System.out.println("共用时：" + span);
        return result;
    }

    //模拟延时
    public void lazy() {
        try {
            int n = (int) new Random().nextInt(500);
            Thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
