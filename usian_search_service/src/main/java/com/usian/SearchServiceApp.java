package com.usian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Title: SearchServiceApp
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/17 14:09
 */
@SpringBootApplication
@MapperScan("com.usian.mapper")
@EnableEurekaClient
public class SearchServiceApp {


    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApp.class,args);
    }
}
