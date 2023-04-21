package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 复制或移动文件请求实体
 *
 * @author: duyubo
 */

@Data
public class CopyOrMoveFileParam implements Serializable {
    private String uid;
    private String vid;
    private String dest;
    private String opera;


    @Override
    public String toString() {
        return "CopyOrMoveFileRequest{" +
                "uid='" + uid + '\'' +
                ", vid='" + vid + '\'' +
                ", dest='" + dest + '\'' +
                ", opera='" + opera + '\'' +
                '}';
    }
}
