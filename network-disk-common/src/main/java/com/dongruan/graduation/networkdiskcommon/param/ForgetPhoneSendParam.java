package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 忘记密码发送短信请求实体
 *
 * @author: duyubo
 */

@Data
public class ForgetPhoneSendParam {
    private String username;
    private String verificationCode;
    private String codeStr;


    @Override
    public String toString() {
        return "ForgetPhoneSendRequest{" +
                "username='" + username + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", codeStr='" + codeStr + '\'' +
                '}';
    }
}
