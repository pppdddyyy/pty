package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Title: ContentWebApp
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/11 14:53
 */
@SpringBootApplication
@EnableFeignClients
public class ContentWebApp {

    public static void main(String[] args) {
        SpringApplication.run(ContentWebApp.class,args);
    }
}
