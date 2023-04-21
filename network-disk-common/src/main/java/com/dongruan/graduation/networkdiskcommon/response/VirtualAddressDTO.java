package com.dongruan.graduation.networkdiskcommon.response;

import lombok.Data;

import java.util.Date;

/**
 * 虚拟地址返回实体
 *
 * @author: duyubo
 */

@Data
public class VirtualAddressDTO {

    private String uuid;

    private String fileId;

    private String userId;

    private String fileName;

    private Integer addrType;

    private String fileMd5;

    private String parentPath;

    private Integer fileSize;

    private Integer dirWhether;

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return "VirtualAddressDTO{" +
                "uuid='" + uuid + '\'' +
                ", fileId='" + fileId + '\'' +
                ", userId='" + userId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", addrType=" + addrType +
                ", fileMd5='" + fileMd5 + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", fileSize=" + fileSize +
                ", dirWhether=" + dirWhether +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
