package com.dongruan.graduation.networkdiskshareservice.service.impl;

import com.dongruan.graduation.networkdiskcommon.param.AddShareViewCountParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateVirtualAddressParam;
import com.dongruan.graduation.networkdiskcommon.param.SaveShareParam;
import com.dongruan.graduation.networkdiskcommon.param.ShareListParam;
import com.dongruan.graduation.networkdiskcommon.param.ShareParam;
import com.dongruan.graduation.networkdiskcommon.response.ShareDTO;
import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.response.VirtualAddressDTO;
import com.dongruan.graduation.networkdiskcommon.utils.BeanUtils;
import com.dongruan.graduation.networkdiskcommon.utils.IDUtils;
import com.dongruan.graduation.networkdiskcommon.utils.JSONUtils;
import com.dongruan.graduation.networkdiskshareservice.dao.ShareDao;
import com.dongruan.graduation.networkdiskshareservice.dao.ShareMapDao;
import com.dongruan.graduation.networkdiskshareservice.entity.ShareDO;
import com.dongruan.graduation.networkdiskshareservice.entity.ShareMapDO;
import com.dongruan.graduation.networkdiskshareservice.remote.CoreRemote;
import com.dongruan.graduation.networkdiskshareservice.remote.UserRemote;
import com.dongruan.graduation.networkdiskshareservice.service.ShareService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class ShareServiceImpl implements ShareService {
    @Autowired
    private ShareDao shareDao;
    @Autowired
    private CoreRemote coreRemote;
    @Autowired
    private ShareMapDao shareMapDao;
    @Autowired
    private UserRemote userRemote;

    @Override
    public String share(ShareParam param) {
        List<String> vidList = JSONUtils.parseObject(param.getVid(), List.class);
        if (vidList == null) {
            return null;
        }
        String fileName = coreRemote.getFilenameByVid(vidList.get(0)).getRespData();
        String shareId = IDUtils.showNextId(new Random().nextInt(30)).toString();
        ShareDO shareDO = new ShareDO();
        shareDO.setCreateTime(new Date());
        shareDO.setDownloadCount(0);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if (param.getExpiration().equals("forever")) {
            c.add(Calendar.YEAR, 100);
            Date newDate = c.getTime();
            shareDO.setExpiration(newDate);
        } else if (param.getExpiration().equals("seven")) {
            c.add(Calendar.DATE, 7);
            Date newDate = c.getTime();
            shareDO.setExpiration(newDate);
        } else if (param.getExpiration().equals("one")) {
            c.add(Calendar.DATE, 1);
            Date newDate = c.getTime();
            shareDO.setExpiration(newDate);
        }
        if (param.getFlag().equals("public")) {
            shareDO.setSharePassword("");
            shareDO.setLockWhether(0);
        } else if (param.getFlag().equals("private")) {
            String words = "ABCDEFGHIJKMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                // 生成一个随机数字
                int index = random.nextInt(words.length());
                // 获得字母数字
                char c1 = words.charAt(index);
                sb.append(c1);
            }
            shareDO.setSharePassword(sb.toString());
            shareDO.setLockWhether(1);
        }
        if (vidList.size() > 1) {
            shareDO.setMultiWhether(1);
        } else {
            shareDO.setMultiWhether(0);
        }
        shareDO.setSaveCount(0);
        shareDO.setShareId(shareId);
        shareDO.setTheme(fileName);
        shareDO.setUserId(param.getUid());
        shareDO.setVisitCount(0);
        shareDao.saveShare(shareDO);
        for (String vid : vidList) {
            ShareMapDO shareMapDO = new ShareMapDO();
            shareMapDO.setVirtualAddressId(vid);
            shareMapDO.setShareId(shareId);
            shareMapDao.saveShareMap(shareMapDO);
        }
        if (param.getFlag().equals("public")) {
            return shareId;
        }
        return shareId + "," + shareDO.getSharePassword();

    }

    @Override
    public Map<String, Object> shareList(ShareListParam param) {
        PageHelper.startPage(param.getPage(), 100);
        if (param.getDesc().equals(1)) {
            PageHelper.orderBy(param.getOrder() + " desc");
        } else {
            PageHelper.orderBy(param.getOrder() + " asc");
        }
        List<ShareDO> shareDOList = shareDao.listShareByUserId(param.getUid());
        if (shareDOList != null && shareDOList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            int i = 0;
            for (ShareDO shareDO : shareDOList) {
                map.put(i++ + "", JSONUtils.toJSONString(shareDO));
            }
            return map;
        }
        return null;
    }

    @Override
    public void unShare(String uid, String shareIds) {
        List<String> shareIdList = JSONUtils.parseObject(shareIds, List.class);
        shareDao.removeShareByShareIdList(uid, shareIdList);
        shareMapDao.removeShareMapByShareIdList(shareIdList);
    }

    @Override
    public Map<String, Object> getShareUser(String shareId) {
        ShareDTO shareDTO = new ShareDTO();
        ShareDO shareDO = shareDao.getShareByShareId(shareId);
        if(shareDO == null){
            return null;
        }
        BeanUtils.copyPropertiesIgnoreNull(shareDO, shareDTO);
        UserInfoDTO userInfoDTO = userRemote.getUserInfo(shareDO.getUserId()).getRespData();
        Map<String, Object> map = new HashMap<>();
        map.put("userinfo", JSONUtils.toJSONString(userInfoDTO));
        map.put("share", JSONUtils.toJSONString(shareDTO));
        return map;
    }

    @Override
    public void saveShare(SaveShareParam param) throws Exception {
        if (verifyLock(param.getLockPassword(), param.getShareId())) {
            List<ShareMapDO> shareMapDOList = shareMapDao.listShareMapByShareId(param.getShareId());
            if (shareMapDOList != null && shareMapDOList.size() > 0) {
                for (ShareMapDO shareMapDO : shareMapDOList) {
                    VirtualAddressDTO virtualAddressDTO = coreRemote.getVirtualaddress(shareMapDO.getVirtualAddressId(), param.getUid()).getRespData();
                    CreateVirtualAddressParam createVirtualAddressParam = new CreateVirtualAddressParam();
                    createVirtualAddressParam.setUid(param.getUid());
                    createVirtualAddressParam.setParentPath(param.getDest());
                    createVirtualAddressParam.setMd5(virtualAddressDTO.getFileMd5());
                    createVirtualAddressParam.setFileType(virtualAddressDTO.getAddrType() + "");
                    createVirtualAddressParam.setFileSize(virtualAddressDTO.getFileSize() + "");
                    createVirtualAddressParam.setFileName(virtualAddressDTO.getFileName());
                    createVirtualAddressParam.setFid(virtualAddressDTO.getFileId());
                    Integer result = coreRemote.createVirtualAddress(createVirtualAddressParam).getRespData();
                    if (result.equals(0)) {
                        throw new Exception("保存失败，容量不足");
                    }
                }
                ShareDO shareDO = shareDao.getShareByShareId(param.getShareId());
                shareDO.setSaveCount(shareDO.getSaveCount() + 1);
                shareDao.updateShare(shareDO);
            } else {
                throw new Exception("分享失效");
            }
        } else {
            throw new Exception("提取密码不正确");
        }
    }

    @Override
    public boolean verifyLock(String lockPassword, String shareId) {
        ShareDO shareDO = shareDao.getShareByShareId(shareId);
        if (shareDO != null) {
            return shareDO.getSharePassword().equals(lockPassword);
        }
        return false;
    }

    @Override
    public int checkLock(String shareId) {
        ShareDO shareDO = shareDao.getShareByShareId(shareId);
        if (shareDO != null) {
            if (shareDO.getLockWhether().equals(1)) {
                return 1;
            }
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, Object> getVInfo(String shareId, String lockPassword) {
        Map<String, Object> map = null;
        if (verifyLock(lockPassword, shareId)) {
            map = new HashMap<>();
            List<ShareMapDO> shareMapDOList = shareMapDao.listShareMapByShareId(shareId);
            List<String> ids = new ArrayList<>();
            if (shareMapDOList != null && shareMapDOList.size() > 0) {
                for (ShareMapDO shareMapDO : shareMapDOList) {
                    ids.add(shareMapDO.getVirtualAddressId());
                    map.put("vid", ids);
                }
            }
            ShareDO shareDO = shareDao.getShareByShareId(shareId);
            map.put("uid", shareDO.getUserId());
        }
        return map;
    }

    @Override
    public void addShareViewCount(String shareId) {
        ShareDO shareDO = shareDao.getShareByShareId(shareId);
        if (shareDO != null) {
            shareDO.setVisitCount(shareDO.getVisitCount() + 1);
        }
        shareDao.updateShare(shareDO);
    }

    @Override
    public void addShareDownloadCount(String shareId) {
        ShareDO shareDO = shareDao.getShareByShareId(shareId);
        if (shareDO != null) {
            shareDO.setDownloadCount(shareDO.getDownloadCount() + 1);
        }
        shareDao.updateShare(shareDO);
    }

}
