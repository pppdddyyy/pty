package com.usian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Title: ItemServiceApp
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/6 8:56
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.usian.mapper")
public class ItemServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApp.class,args);
    }
}
