package com.dongruan.graduation.networkdiskcoreservice.service.impl;

import com.dongruan.graduation.networkdiskcommon.utils.JSONUtils;
import com.dongruan.graduation.networkdiskcoreservice.dao.CapacityDao;
import com.dongruan.graduation.networkdiskcoreservice.entity.CapacityDO;
import com.dongruan.graduation.networkdiskcoreservice.service.CapacityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CapacityServiceImpl implements CapacityService {
    @Autowired
    private CapacityDao capacityDao;

    @Override
    public String getUseCapacity(String userId) {
        return JSONUtils.toJSONString(capacityDao.getCapacity(userId));
    }

    @Override
    public Integer updateCapacity(CapacityDO capacityDO) {
        return capacityDao.updateCapacity(capacityDO);
    }

    @Override
    public Integer saveCapacity(String uid) {
        CapacityDO capacityDO = new CapacityDO();
        capacityDO.setUserId(uid);
        capacityDO.setTotalCapacity(5368709120L);
        capacityDO.setUsedCapacity(0L);
        return capacityDao.saveCapacity(capacityDO);
    }
}
