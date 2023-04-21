package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 用户注册请求实体
 *
 * @author: quhailong
 * @date: 2019/9/25
 */

@Data
public class UserRegisterParam {
    private String phone;
    private String username;
    private String password;
    private String verifyCode;
    private String rsaKey;
    private String pid;

    @Override
    public String toString() {
        return "UserRegisterRequest{" +
                "phoneNum='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", rsaKey='" + rsaKey + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
