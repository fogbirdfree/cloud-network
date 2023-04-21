package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建文件夹请求实体
 *
 * @author: duyubo
 */

@Data
public class CreateDirParam implements Serializable {
    private String dirName;
    private String uid;
    private String parentPath;


    @Override
    public String toString() {
        return "CreateDirRequest{" +
                "dirName='" + dirName + '\'' +
                ", uid='" + uid + '\'' +
                ", parentPath='" + parentPath + '\'' +
                '}';
    }
}
