package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 秒传请求实体
 *
 * @author: duyubo
 */

@Data
public class QuickUploadFileParam implements Serializable {
    private String fid;
    private String fileName;
    private String md5;
    private String uid;
    private String parentPath;

    @Override
    public String toString() {
        return "QuickUploadFileRequest{" +
                "fid='" + fid + '\'' +
                ", fileName='" + fileName + '\'' +
                ", md5='" + md5 + '\'' +
                ", uid='" + uid + '\'' +
                ", parentPath='" + parentPath + '\'' +
                '}';
    }
}
