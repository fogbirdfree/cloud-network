package com.dongruan.graduation.networkdiskshareservice.service.impl;

import com.dongruan.graduation.networkdiskshareservice.dao.ShareMapDao;
import com.dongruan.graduation.networkdiskshareservice.entity.ShareMapDO;
import com.dongruan.graduation.networkdiskshareservice.service.ShareMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShareMapServiceImpl implements ShareMapService {
    @Autowired
    private ShareMapDao shareMapDao;

    @Override
    public Integer saveShareMap(ShareMapDO shareMapDO) {
        return shareMapDao.saveShareMap(shareMapDO);
    }

    @Override
    public Integer removeShareMapByShareIdList(List<String> shareIdList) {
        return shareMapDao.removeShareMapByShareIdList(shareIdList);
    }

    @Override
    public List<ShareMapDO> listShareMapByShareId(String shareId) {
        return shareMapDao.listShareMapByShareId(shareId);
    }
}
