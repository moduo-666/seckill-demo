package com.moduo.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan("com.moduo.seckill.mapper")
public class SeckillDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class,args);
//        SpringApplication.run(SeckillDemo.class, args);
    }

}
