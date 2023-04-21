package com.dongruan.graduation.networkdiskcoreservice.controller;

import com.dongruan.graduation.networkdiskcommon.param.CopyOrMoveFileParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateDirParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateVirtualAddressParam;
import com.dongruan.graduation.networkdiskcommon.param.RenameFileOrDirParam;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskcoreservice.service.VirtualAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新服务
 *
 * @author: duyubo
 */
@RestController
@RequestMapping(value = "/core")
public class UpdateContentController {
    @Autowired
    private VirtualAddressService virtualAddressService;

    /**
     * 文件或文件夹重命名
     *
     * @author: duyubo
     */
    @PutMapping(value = "renameFileOrDir")
    public RestResult<String> renameFileOrDir(@RequestBody RenameFileOrDirParam param) {
        RestResult<String> result = new RestResult<>();
        boolean res = virtualAddressService.renameFileOrDir(param);
        if (res) {
            result.setRespData("200");
            result.setRespCode(200);
        } else {
            result.setRespCode(203);
            result.setRespData("有重名");
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @author: duyubo
     */
    @DeleteMapping(value = "deleteFile")
    public RestResult<String> deleteFile(String vid) {
        RestResult<String> result = new RestResult<>();
        virtualAddressService.deleteFile(vid);
        result.success(null);
        result.setDataCode("200");
        return result;
    }

    /**
     * 创建文件夹
     *
     * @author: duyubo
     */
    @PostMapping(value = "createDir")
    public RestResult<String> createDir(@RequestBody CreateDirParam request) {
        RestResult<String> result = new RestResult<>();
        Map<String, Object> map = new HashMap<>();
        String res;
        try {
            res = virtualAddressService.createDir(request);
        } catch (Exception e) {
            result.error(e.getMessage());
            return result;
        }
        map.put("0", res);
        result.setRespMap(map);
        result.setRespData("200");
        return result;
    }

    /**
     * 文件复制或移动
     *
     * @author: duyubo
     */
    @PutMapping(value = "copyOrMoveFile")
    public RestResult<String> copyOrMoveFile(@RequestBody CopyOrMoveFileParam param) {
        RestResult<String> result = new RestResult<>();
        try {
            virtualAddressService.copyOrMoveFile(param);
            result.success(null);
            result.setDataCode("200");
        } catch (Exception e) {
            result.error(e.getMessage());
        }
        return result;
    }

    /**
     * 创建文件(调用)
     *
     * @author: duyubo
     */
    @PostMapping(value = "createVirtualAddress")
    public RestResult<Integer> createVirtualAddress(@RequestBody CreateVirtualAddressParam param) {
        RestResult<Integer> result = new RestResult<>();
        result.success(virtualAddressService.createVirtualAddress(param));
        return result;
    }

}
