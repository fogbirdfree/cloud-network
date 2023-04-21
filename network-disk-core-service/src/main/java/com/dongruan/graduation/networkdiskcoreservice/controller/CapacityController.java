package com.dongruan.graduation.networkdiskcoreservice.controller;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskcoreservice.service.CapacityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 容量服务
 *
 * @author: duyubo
 */
@RestController
@RequestMapping(value = "/core")
public class CapacityController {
    @Autowired
    private CapacityService capacityService;

    /**
     * 查询使用容量
     *
     * @author: duyubo
     */
    @GetMapping(value = "useCapacity")
    public RestResult<String> useCapacity(String uid) {
        RestResult<String> result = new RestResult<>();
        result.success(null);
        result.setRespData(capacityService.getUseCapacity(uid));
        return result;
    }

    /**
     * 初始化容量
     *
     * @author: duyubo
     */
    @GetMapping(value = "initCapacity")
    public RestResult<Integer> initCapacity(@RequestParam("userId") String userId) {
        RestResult<Integer> result = new RestResult<>();
        result.success(capacityService.saveCapacity(userId));
        return result;
    }

}
