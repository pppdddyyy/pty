package com.usian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Title: ContentServiceApp
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/11 14:57
 */
@SpringBootApplication
@MapperScan("com.usian.mapper")
public class ContentServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApp.class,args);
    }
}
