package com.chengtai.fashionmgmt;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.chengtai.fashionmgmt", annotationClass = Mapper.class)
public class FashionMgmtApplication {
    public static void main(String[] args) {
        SpringApplication.run(FashionMgmtApplication.class, args);
    }
}


