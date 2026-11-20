package com.chengtai.fashionmgmt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chengtai.fashionmgmt")
public class FashionMgmtApplication {
    public static void main(String[] args) {
        SpringApplication.run(FashionMgmtApplication.class, args);
    }
}
