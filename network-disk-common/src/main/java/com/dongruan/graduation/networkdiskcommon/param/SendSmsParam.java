package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 发送短信请求实体
 *
 * @author: duyubo
 */

@Data
public class SendSmsParam {
     private String sid;
     private String token;
     private String appId;
     private String templateId;
     private String param;
     private String mobile;
     private String uid;

    @Override
    public String toString() {
        return "SendSmsRequest{" +
                "sid='" + sid + '\'' +
                ", token='" + token + '\'' +
                ", appId='" + appId + '\'' +
                ", templateId='" + templateId + '\'' +
                ", param='" + param + '\'' +
                ", mobile='" + mobile + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
