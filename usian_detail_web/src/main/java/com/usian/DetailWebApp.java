package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Title: DetailWeb
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/19 10:58
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class DetailWebApp {

    public static void main(String[] args) {
        SpringApplication.run(DetailWebApp.class,args);
    }
}
