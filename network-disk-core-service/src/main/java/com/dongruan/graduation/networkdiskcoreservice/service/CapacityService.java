package com.dongruan.graduation.networkdiskcoreservice.service;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskcoreservice.entity.CapacityDO;

public interface CapacityService {
    /**
     * 获取容量
     *
     * @author: duyubo
     */
    String getUseCapacity(String userId);

    /**
     * 修改容量
     *
     * @author: duyubo
     */
    Integer updateCapacity(CapacityDO capacityDO);

    /**
     * 保存容量
     *
     * @author: duyubo
     */
    Integer saveCapacity(String uid);
}
