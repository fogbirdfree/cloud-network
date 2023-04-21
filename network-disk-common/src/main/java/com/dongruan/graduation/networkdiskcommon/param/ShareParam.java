package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 分享请求实体
 *
 * @author: duyubo
 */

@Data
public class ShareParam {
    private String expiration;
    private String flag;
    private String uid;
    private String vid;

    @Override
    public String toString() {
        return "ShareRequest{" +
                "expiration='" + expiration + '\'' +
                ", flag='" + flag + '\'' +
                ", uid='" + uid + '\'' +
                ", vid='" + vid + '\'' +
                '}';
    }
}
