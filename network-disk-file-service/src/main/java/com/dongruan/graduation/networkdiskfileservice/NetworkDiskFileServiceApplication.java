package com.dongruan.graduation.networkdiskfileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NetworkDiskFileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkDiskFileServiceApplication.class, args);
    }

}
