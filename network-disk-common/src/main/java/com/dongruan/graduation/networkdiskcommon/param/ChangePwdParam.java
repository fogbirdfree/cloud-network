package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 修改密码请求实体
 *
 * @author: duyubo
 */

@Data
public class ChangePwdParam {
    private String username;
    private String password;
    private String newPassword;
    private String rsaKey;

    @Override
    public String toString() {
        return "ChangePwdRequest{" +
                ", newPassword='" + newPassword + '\'' +
                ", rsaKey='" + rsaKey + '\'' +
                '}';
    }
}
