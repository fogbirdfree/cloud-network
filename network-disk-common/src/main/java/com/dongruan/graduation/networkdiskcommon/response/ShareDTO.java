package com.dongruan.graduation.networkdiskcommon.response;

import lombok.Data;

import java.util.Date;

/**
 * 分享返回实体
 *
 * @author: duyubo
 */

@Data
public class ShareDTO {

    private String shareId;

    private String theme;

    private String userId;

    private Integer lockWhether;

    private String sharePassword;

    private Integer visitCount;

    private Integer saveCount;

    private Integer downloadCount;

    private Integer multiWhether;

    private Date expiration;

    @Override
    public String toString() {
        return "ShareDTO{" +
                "shareId='" + shareId + '\'' +
                ", theme='" + theme + '\'' +
                ", userId='" + userId + '\'' +
                ", lockWhether=" + lockWhether +
                ", sharePassword='" + sharePassword + '\'' +
                ", visitCount=" + visitCount +
                ", saveCount=" + saveCount +
                ", downloadCount=" + downloadCount +
                ", multiWhether=" + multiWhether +
                ", expiration=" + expiration +
                '}';
    }
}
