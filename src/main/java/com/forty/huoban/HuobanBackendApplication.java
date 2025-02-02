package com.forty.huoban;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.forty.huoban.mapper")
@EnableScheduling
public class HuobanBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuobanBackendApplication.class, args);
    }

}
