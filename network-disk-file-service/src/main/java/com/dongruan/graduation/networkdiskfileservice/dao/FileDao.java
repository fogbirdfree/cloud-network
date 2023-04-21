package com.dongruan.graduation.networkdiskfileservice.dao;

import com.dongruan.graduation.networkdiskfileservice.entity.FileDO;
import org.apache.ibatis.annotations.Param;
public interface FileDao {

    /**
     * 查询md5是否已经存在
     *
     * @author: duyubo
     */
    Integer checkMd5Whether(@Param("fileMd5") String fileMd5);

    /**
     * 根据Md5获取文件信息
     *
     * @author: duyubo
     */
    FileDO getFileByMd5(@Param("fileMd5") String fileMd5);

    /**
     * 保存文件信息
     *
     * @author: duyubo
     */
    Integer saveFile(FileDO fileDO);

    /**
     * 根据文件ID获取文件信息
     *
     * @author: duyubo
     */
    FileDO getFileByFid(@Param("fileId") String fileId);
}