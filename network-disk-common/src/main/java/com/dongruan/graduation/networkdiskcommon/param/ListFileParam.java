package com.dongruan.graduation.networkdiskcommon.param;

import lombok.Data;

/**
 * 文件列表请求实体
 *
 * @author: duyubo
 */

@Data
public class ListFileParam {
    private String type;
    private String uid;
    private String path;
    private Integer page;
    private String order;
    private Integer desc;

    @Override
    public String toString() {
        return "ListFileRequest{" +
                "type='" + type + '\'' +
                ", uid='" + uid + '\'' +
                ", path='" + path + '\'' +
                ", page=" + page +
                ", order='" + order + '\'' +
                ", desc=" + desc +
                '}';
    }
}
