package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Title: PortalWeb
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/12 14:10
 */
@SpringBootApplication
@EnableFeignClients
public class PortalWebApp {
    public static void main(String[] args) {
        SpringApplication.run(PortalWebApp.class,args);
    }
}
