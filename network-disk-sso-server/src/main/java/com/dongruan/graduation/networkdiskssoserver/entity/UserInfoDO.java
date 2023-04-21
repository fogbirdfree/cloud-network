package com.dongruan.graduation.networkdiskssoserver.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author duyubo
 */
@Data
public class UserInfoDO {
    private Integer id;

    private String userId;

    private String userName;

    private String password;

    private String phone;

    private String salt;

    private String picLocation;

    private Date createTime;

    private Date updateTime;
}