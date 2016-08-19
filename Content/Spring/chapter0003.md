
# Spring AOP 简介

因为某个对象消耗太多资源,而且你的代码并不是每个逻辑路径都需要此对象, 你曾有过延迟创建对象的想法吗 ( if和else就是不同的两条逻辑路径) ? 你有想过限制访问某个对象,也就是说,提供一组方法给普通用户,特别方法给管理员用户?以上两种需求都非常类似，并且都需要解决一个更大的问题:你如何提供一致的接口给某个对象让它可以改变其内部功能,或者是从来不存在的功能? 可以通过引入一个新的对象，来实现对真实对象的操作或者将新的对象作为真实对象的一个替身。即代理对象。它可以在客户端和目标对象之间起到中介的作用，并且可以通过代理对象去掉客户不能看到的内容和服务或者添加客户需要的额外服务。

例子1：经典例子就是网络代理，你想访问facebook或者twitter ，如何绕过GFW，找个代理网站。

例子2：可以调用远程代理处理一些操作如图：

 ![](../../images/Spring/000019.png)
 
 2.问题:

你怎样才能在不直接操作对象的情况下,对此对象进行访问?

3.解决方案

代理模式: 为其他对象提供一种代理，并以控制对这个对象的访问。（Provide asurrogate or placeholder foranother object tocontrol access to it. ）而对一个对象进行访问控制的一个原因是为了只有在我们确实需要这个对象时才对它进行创建和初始化。它是给某一个对象提供一个替代者(占位者),使之在client对象和subject对象之间编码更有效率。代理可以提供延迟实例化(lazy instantiation),控制访问, 等等，包括只在调用中传递。 一个处理纯本地资源的代理有时被称作虚拟代理。远程服务的代理常常称为远程代理。强制 控制访问的代理称为保护代理。

4.实用性

在需要用比较通用和复杂的对象指针代替简单的指针的时候，使用 Proxy模式。下面是一些可以使用Proxy模式常见情况：
1) 远程代理（Remote  Proxy）为一个位于不同的地址空间的对象提供一个本地的代理对象。这个不同的地址空间可以是在同一台主机中，也可是在另一台主机中，远程代理又叫做大使(Ambassador)
2) 虚拟代理（Virtual Proxy）根据需要创建开销很大的对象。如果需要创建一个资源消耗较大的对象，先创建一个消耗相对较小的对象来表示，真实对象只在需要时才会被真正创建。 
3) 保护代理（Protection Proxy）控制对原始对象的访问。保护代理用于对象应该有不同的访问权限的时候。
4) 智能指引（Smart Reference）取代了简单的指针，它在访问对象时执行一些附加操作。
5) Copy-on-Write代理：它是虚拟代理的一种，把复制（克隆）操作延迟到只有在客户端真正需要时才执行。一般来说，对象的深克隆是一个开销较大的操作，Copy-on-Write代理可以让这个操作延迟，只有对象被用到的时候才被克隆。

 ![](../../images/Spring/000020.jpg)
 
##例子

我们创建一个Math类

```
package com.proxy;

/**
 * Created by chuguangming on 16/8/19.
 */
public class Math {
    //加
    public int add(int n1,int n2){
        int result=n1+n2;
        System.out.println(n1+"+"+n2+"="+result);
        return result;
    }


    //减
    public int sub(int n1,int n2){
        int result=n1-n2;
        System.out.println(n1+"-"+n2+"="+result);
        return result;
    }

    //乘
    public int mut(int n1,int n2){
        int result=n1*n2;
        System.out.println(n1+"X"+n2+"="+result);
        return result;
    }

    //除
    public int div(int n1,int n2){
        int result=n1/n2;
        System.out.println(n1+"/"+n2+"="+result);
        return result;
    }
}

```

现在需求发生了变化，要求项目中所有的类在执行方法时输出执行耗时。最直接的办法是修改源代码，如下所示：

```
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

```

测试运行：

```
package com.proxy;

import com.springioc.annotation.demo.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chuguangming on 16/8/19.
 */
public class Test {

    @org.junit.Test
    public void test01()
    {
        Math math=new Math();
        int n1=100,n2=5;
        math.add(n1, n2);
        math.sub(n1, n2);
        math.mut(n1, n2);
        math.div(n1, n2);
    }
    
    @org.junit.Test
    public void test02()
    {
        Math2 math=new Math2();
        int n1=100,n2=5;
        math.add(n1, n2);
        math.sub(n1, n2);
        math.mut(n1, n2);
        math.div(n1, n2);
    }
}

```

缺点：

1、工作量特别大，如果项目中有多个类，多个方法，则要修改多次。

2、违背了设计原则：开闭原则（OCP），对扩展开放，对修改关闭，而为了增加功能把每个方法都修改了，也不便于维护。

3、违背了设计原则：单一职责（SRP），每个方法除了要完成自己本身的功能，还要计算耗时、延时；每一个方法引起它变化的原因就有多种。

4、违背了设计原则：依赖倒转（DIP），抽象不应该依赖细节，两者都应该依赖抽象。而在Test类中，Test与Math都是细节。

使用静态代理可以解决部分问题。