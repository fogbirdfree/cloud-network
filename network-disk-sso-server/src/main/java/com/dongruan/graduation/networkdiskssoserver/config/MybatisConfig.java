package com.dongruan.graduation.networkdiskssoserver.config;

/**
 * @author: duyubo
 * @date: 2021年02月22日, 0022 09:16
 * @description:
 */
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(value = "com.dongruan.graduation.networkdiskssoserver.dao")
public class MybatisConfig {
}

