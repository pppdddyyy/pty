package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Title: SearchWebApp
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/17 14:14
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class SearchWebApp {

    public static void main(String[] args) {
        SpringApplication.run(SearchWebApp.class,args);
    }
}
