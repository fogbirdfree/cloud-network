package com.dongruan.graduation.networkdiskcoreservice.service;


import com.dongruan.graduation.networkdiskcommon.param.CheckDirWhetherParam;
import com.dongruan.graduation.networkdiskcommon.param.CopyOrMoveFileParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateDirParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateVirtualAddressParam;
import com.dongruan.graduation.networkdiskcommon.param.ListFileParam;
import com.dongruan.graduation.networkdiskcommon.param.ListFolderParam;
import com.dongruan.graduation.networkdiskcommon.param.RenameFileOrDirParam;
import com.dongruan.graduation.networkdiskcommon.param.SearchFileParam;
import com.dongruan.graduation.networkdiskcoreservice.entity.VirtualAddressDO;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface VirtualAddressService {

    /**
     * 获取虚拟地址信息
     *
     * @author: duyubo
     */
    List<VirtualAddressDO> listVirtualAddress(ListFileParam param) throws UnsupportedEncodingException;

    /**
     * 获取文件信息
     *
     * @author: duyubo
     */
    Map<String, Object> listFolder(ListFolderParam param) throws UnsupportedEncodingException;

    /**
     * 搜索文件
     *
     * @author: duyubo
     */
    Map<String, Object> listVirtualAddressLikeFileName(SearchFileParam param);

    /**
     * 判断重名
     *
     * @author: duyubo
     */
    Integer checkVirtualAddress(CheckDirWhetherParam param);

    /**
     * 获取虚拟地址信息
     *
     * @author: duyubo
     */
    VirtualAddressDO getVirtualAddress(String vid);

    void deleteFile(String vid);

    boolean renameFileOrDir(RenameFileOrDirParam param);

    void copyOrMoveFile(CopyOrMoveFileParam param) throws Exception;

    String createDir(CreateDirParam param) throws Exception;

    Integer createVirtualAddress(CreateVirtualAddressParam param);

    /**
     * 修改虚拟地址信息
     *
     * @author: duyubo
     */
//    RestResult<String> updateVirtualAddress(VirtualAddressDO virtualAddressDO);

    /**
     * 获取父路径相关的虚拟地址
     *
     * @author: duyubo
     */
//    RestResult<String> listVirtualAddressLikeFilePath(String userId, String parentPath);

    /**
     * 删除虚拟地址
     *
     * @author: duyubo
     */
//    RestResult<String> removeVirtualAddress(String uuid);


    /**
     * 保存虚拟地址
     *
     * @author: duyubo
     */
//    RestResult<String> saveVirtualAddress(VirtualAddressDO virtualAddressDO);
}
