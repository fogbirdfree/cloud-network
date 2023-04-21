package com.dongruan.graduation.networkdiskshareservice.config;

/**
 * @author: duyubo
 * @date: 2020年11月24日, 0024 11:24
 * @description: MyBatis相关配置
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(value = "com.dongruan.graduation.networkdiskshareservice.dao")
public class MybatisConfig {
}

