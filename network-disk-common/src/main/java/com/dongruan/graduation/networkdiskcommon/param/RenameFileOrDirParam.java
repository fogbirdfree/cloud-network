package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 重命名文件或文件夹请求实体
 *
 * @author: duyubo
 */

@Data
public class RenameFileOrDirParam implements Serializable {
    private String uid;
    private String newName;
    private String vid;
    private String flag;


    @Override
    public String toString() {
        return "RenameFileOrDirRequest{" +
                "uid='" + uid + '\'' +
                ", newName='" + newName + '\'' +
                ", vid='" + vid + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
