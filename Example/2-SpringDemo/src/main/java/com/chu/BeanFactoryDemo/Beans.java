package com.chu.BeanFactoryDemo;

/**
 * Created by chuguangming on 2016/10/26.
 */
import com.chu.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean(name = "car")
    public Car buildCar()
    {
        com.chu.Car car=new com.chu.Car();
        car.setBrand("红旗");
        car.setMaxSpeed(200);
        return  car;
    }
}
