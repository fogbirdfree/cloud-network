package com.dongruan.graduation.networkdiskshareservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NetworkDiskShareServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkDiskShareServiceApplication.class, args);
    }

}
