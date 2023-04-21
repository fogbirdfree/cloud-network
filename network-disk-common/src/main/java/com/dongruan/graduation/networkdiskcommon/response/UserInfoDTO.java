package com.dongruan.graduation.networkdiskcommon.response;

import lombok.Data;

/**
 * 用户信息返回实体
 *
 * @author: duyubo
 */

@Data
public class UserInfoDTO {
    private Integer id;

    private String userId;

    private String userName;

    private String phone;

    private String picLocation;


    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", picLocation='" + picLocation + '\'' +
                '}';
    }
}
