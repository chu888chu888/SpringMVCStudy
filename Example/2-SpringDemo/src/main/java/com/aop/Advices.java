package com.aop;

/**
 * Created by chuguangming on 16/8/19.
 */
import org.aspectj.lang.JoinPoint;

/**
 * 通知类，横切逻辑
 *
 */
public class Advices {

    public void before(JoinPoint jp){
        System.out.println("----------前置通知----------");
        System.out.println(jp.getSignature().getName());
    }

    public void after(JoinPoint jp){
        System.out.println("----------最终通知----------");
    }
}
