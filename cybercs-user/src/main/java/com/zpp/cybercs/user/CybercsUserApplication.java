package com.zpp.cybercs.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.zpp.cybercs")
@MapperScan("com.zpp.cybercs.user.mapper")
public class CybercsUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CybercsUserApplication.class, args);
    }

}
