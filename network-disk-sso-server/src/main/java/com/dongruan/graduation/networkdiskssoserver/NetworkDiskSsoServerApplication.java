package com.dongruan.graduation.networkdiskssoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetworkDiskSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkDiskSsoServerApplication.class, args);
    }

}
