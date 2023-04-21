package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建虚拟地址请求实体
 *
 * @author: duyubo
 */

@Data
public class CreateVirtualAddressParam implements Serializable {
    private String fid;
    private String uid;
    private String fileName;
    private String md5;
    private String fileType;
    private String fileSize;
    private String parentPath;

    @Override
    public String toString() {
        return "CreateVirtualAddressRequest{" +
                "fid='" + fid + '\'' +
                ", uid='" + uid + '\'' +
                ", fileName='" + fileName + '\'' +
                ", md5='" + md5 + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", parentPath='" + parentPath + '\'' +
                '}';
    }
}
