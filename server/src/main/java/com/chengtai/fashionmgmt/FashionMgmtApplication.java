package com.chengtai.fashionmgmt;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.chengtai.fashionmgmt", annotationClass = Mapper.class)
public class FashionMgmtApplication {
    /**
     * 启动成泰服装管理系统 Spring Boot 应用。
     */
    public static void main(String[] args) {
        SpringApplication.run(FashionMgmtApplication.class, args);
    }
}


