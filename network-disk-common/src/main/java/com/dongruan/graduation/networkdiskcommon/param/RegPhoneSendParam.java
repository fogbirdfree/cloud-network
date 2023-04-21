package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 注册发送短信请求实体
 *
 * @author: duyubo
 */

@Data
public class RegPhoneSendParam {
    private String phoneNum;
    private String verificationCode;
    private String codeStr;

    @Override
    public String toString() {
        return "RegPhoneSendRequest{" +
                "phoneNum='" + phoneNum + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", codeStr='" + codeStr + '\'' +
                '}';
    }
}
