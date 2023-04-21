package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 保存分享请求实体
 *
 * @author: duyubo
 */

@Data
public class SaveShareParam {
    private String lockPassword;
    private String shareId;
    private String dest;
    private String uid;

    @Override
    public String toString() {
        return "SaveShareRequest{" +
                "lockPassword='" + lockPassword + '\'' +
                ", shareId='" + shareId + '\'' +
                ", dest='" + dest + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
