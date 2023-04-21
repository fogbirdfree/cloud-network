package com.dongruan.graduation.networkdiskshareservice.service;

import com.dongruan.graduation.networkdiskcommon.param.AddShareViewCountParam;
import com.dongruan.graduation.networkdiskcommon.param.SaveShareParam;
import com.dongruan.graduation.networkdiskcommon.param.ShareListParam;
import com.dongruan.graduation.networkdiskcommon.param.ShareParam;

import java.util.Map;

public interface ShareService {

    String share(ShareParam param);

    Map<String, Object> shareList(ShareListParam param);

    void unShare(String uid, String shareIds);

    Map<String, Object> getShareUser(String shareId);

    void saveShare(SaveShareParam param) throws Exception;

    int checkLock(String shareId);

    boolean verifyLock(String lockPassword, String shareId);

    void addShareViewCount(String shareId);

    void addShareDownloadCount(String shareId);

    Map<String, Object> getVInfo(String shareId, String lockPassword);

}
