package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索文件请求实体
 *
 * @author: duyubo
 */

@Data
public class SearchFileParam implements Serializable {
    private String uid;
    private String key;
    private Integer page;
    private String order;
    private Integer desc;

    @Override
    public String toString() {
        return "SearchFileRequest{" +
                "uid='" + uid + '\'' +
                ", key='" + key + '\'' +
                ", page=" + page +
                ", order='" + order + '\'' +
                ", desc=" + desc +
                '}';
    }
}
