package com.skillexchange;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.skillexchange.mapper")
public class SkillExchangeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkillExchangeApplication.class, args);
    }
}