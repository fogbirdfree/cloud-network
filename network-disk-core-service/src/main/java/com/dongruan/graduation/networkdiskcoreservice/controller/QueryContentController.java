package com.dongruan.graduation.networkdiskcoreservice.controller;

import com.dongruan.graduation.networkdiskcommon.param.CheckDirWhetherParam;
import com.dongruan.graduation.networkdiskcommon.param.ListFileParam;
import com.dongruan.graduation.networkdiskcommon.param.ListFolderParam;
import com.dongruan.graduation.networkdiskcommon.param.SearchFileParam;
import com.dongruan.graduation.networkdiskcommon.response.VirtualAddressDTO;
import com.dongruan.graduation.networkdiskcommon.utils.JSONUtils;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskcoreservice.entity.VirtualAddressDO;
import com.dongruan.graduation.networkdiskcoreservice.service.VirtualAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询服务
 *
 * @author: duyubo
 */
@RestController
@RequestMapping(value = "/core")
public class QueryContentController {
    @Autowired
    private VirtualAddressService virtualAddressService;

    /**
     * 查询文件列表
     *
     * @author: duyubo
     */
    @GetMapping(value = "listFile")
    public RestResult<String> listFile(ListFileParam param) throws UnsupportedEncodingException {
        RestResult<String> result = new RestResult<>();
        Map<String, Object> map = new HashMap<>();
        List<VirtualAddressDO> virtualAddressDOList = virtualAddressService.listVirtualAddress(param);
        if (virtualAddressDOList != null && virtualAddressDOList.size() > 0) {
            int i = 0;
            for (VirtualAddressDO virtualAddressDO : virtualAddressDOList) {
                map.put(i++ + "", JSONUtils.toJSONString(virtualAddressDO));
            }
            result.setRespMap(map);
            result.setRespData("200");
        } else {
            result.success(null);
        }
        return result;
    }

    /**
     * 展示文件夹列表
     *
     * @author: duyubo
     */
    @GetMapping(value = "listFolder")
    public RestResult<String> listFolder(ListFolderParam param) throws Exception {
        RestResult<String> result = new RestResult<>();
        Map<String, Object> map = virtualAddressService.listFolder(param);
        if (map != null) {
            result.setRespMap(map);
            result.setRespData("200");
        } else {
            result.success(null);
        }
        return result;
    }

    /**
     * 搜索文件
     *
     * @author: duyubo
     */
    @GetMapping(value = "searchFile")
    public RestResult<String> searchFile(SearchFileParam param) {
        RestResult<String> result = new RestResult<>();
        Map<String, Object> map = virtualAddressService.listVirtualAddressLikeFileName(param);
        result.setRespData("200");
        result.setRespMap(map);
        return result;
    }

    /**
     * 查询文件夹是否存在(调用)
     *
     * @author: duyubo
     */
    @GetMapping(value = "checkDirWhether")
    public RestResult<Integer> checkDirWhether(CheckDirWhetherParam param) {
        RestResult<Integer> result = new RestResult<>();
        result.success(virtualAddressService.checkVirtualAddress(param));
        return result;
    }

    /**
     * 根据虚拟地址ID获取文件名称(调用)
     *
     * @author: duyubo
     */
    @GetMapping(value = "getFilenameByVid")
    public RestResult<String> getFileNameByVid(String vid) {
        RestResult<String> result = new RestResult<>();
        VirtualAddressDO virtualAddress = virtualAddressService.getVirtualAddress(vid);
        result.success(virtualAddress.getFileName());
        return result;
    }

    /**
     * 根据虚拟地址ID获取实体
     *
     * @author: duyubo
     */
    @GetMapping(value = "getVirtualAddress")
    public RestResult<VirtualAddressDTO> getVirtualAddress(String vid) {
        RestResult<VirtualAddressDTO> result = new RestResult<>();
        VirtualAddressDTO virtualAddressDTO = new VirtualAddressDTO();
        VirtualAddressDO virtualAddressDO = virtualAddressService.getVirtualAddress(vid);
        BeanUtils.copyProperties(virtualAddressDO, virtualAddressDTO);
        result.success(virtualAddressDTO);
        return result;
    }
}
