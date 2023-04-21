package com.dongruan.graduation.networkdiskshareservice.controller;

import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskshareservice.remote.UserRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: duyubo
 * @date: 2021年03月02日, 0002 16:19
 * @description:
 */

@Controller
@RequestMapping("/share")
public class pageController {
    @Autowired
    private UserRemote userRemote;

    @GetMapping("/manage/{uid}")
    public String share(Model model, @PathVariable("uid") String uid) {
        UserInfoDTO userInfo = userRemote.getUserInfo(uid).getRespData();
        model.addAttribute("username", userInfo.getUserName());
        model.addAttribute("uid", uid);
        return "share";
    }
}
