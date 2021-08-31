package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Title: ItemWebApp
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/6 9:36
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ItemWebApp {

    public static void main(String[] args) {
        SpringApplication.run(ItemWebApp.class,args);
    }
}
