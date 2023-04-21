package com.dongruan.graduation.networkdiskshareservice.service.impl;

import com.dongruan.graduation.networkdiskcommon.param.AddShareViewCountParam;
import com.dongruan.graduation.networkdiskcommon.response.ShareDTO;
import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.JSONUtils;
import com.dongruan.graduation.networkdiskshareservice.remote.UserRemote;
import com.dongruan.graduation.networkdiskshareservice.service.PageService;
import com.dongruan.graduation.networkdiskshareservice.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * @author: duyubo
 * @date: 2021年04月02日, 0002 14:33
 * @description:
 */

@Component
public class PageServiceImpl implements PageService {

    @Autowired
    private ShareService shareService;
    @Autowired
    private UserRemote userRemote;

    @Override
    public String sHandle(Model model, String shareId, String uid) {
        Map<String, Object> respMsg = shareService.getShareUser(shareId);
        UserInfoDTO shareUserInfoDTO = JSONUtils.parseObject((String) respMsg.get("userinfo"), UserInfoDTO.class);
        UserInfoDTO userInfo = userRemote.getUserInfo(uid).getRespData();
        if (shareUserInfoDTO == null) {
            model.addAttribute("name", userInfo.getUserName());
            model.addAttribute("uid", userInfo.getId());
            return "sError";
        } else if (shareUserInfoDTO.getId().toString().equals(uid)) {
            model.addAttribute("name", userInfo.getUserName());
            model.addAttribute("uid", userInfo.getId());
            return "share";
        }
        ShareDTO shareDTO = JSONUtils.parseObject((String) respMsg.get("share"), ShareDTO.class);
        if (shareDTO.getExpiration() != null && shareDTO.getExpiration().getTime() - (System.currentTimeMillis()) < 0) {
            model.addAttribute("name", userInfo.getUserName());
            model.addAttribute("uid", userInfo.getId());
            return "sError";
        }
        model.addAttribute("name", userInfo.getUserName());
        model.addAttribute("shareUser", shareUserInfoDTO.getUserName());
        if (shareDTO.getMultiWhether() == 1) {
            model.addAttribute("shareName", shareDTO.getTheme() + "等文件");
        } else {
            model.addAttribute("shareName", shareDTO.getTheme() + "文件");
        }
        shareService.addShareViewCount(shareId);
        model.addAttribute("shareId", shareDTO.getShareId());
        model.addAttribute("name", userInfo.getUserName());
        model.addAttribute("uid", userInfo.getId());
        return "s";
    }
}
