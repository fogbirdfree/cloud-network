package com.dongruan.graduation.networkdiskuser.controller;

import com.dongruan.graduation.networkdiskcommon.param.ChangePwdParam;
import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskuser.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: duyubo
 * @date: 2021年04月06日, 0006 14:36
 * @description:
 */

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userService;
    /**
     * 修改密码
     *
     * @author: duyubo
     */
    @PostMapping(value = "modifyPassword")
    public RestResult<String> modifyPassword(@RequestBody ChangePwdParam request) {
        return userService.modifyPassword(request);
    }

    @GetMapping("getUserInfo/{userId}")
    public RestResult<UserInfoDTO> getUserInfo(@PathVariable("userId") String userId) {
        return userService.getUserInfo(userId);
    }

    /**
     * 加载用户头像
     *
     * @author: duyubo
     */
    @GetMapping(value = "/loadImg")
    public RestResult<String> loadImg(@RequestParam("uid") String uid) {
        return userService.loadImgHandle(uid);
    }

    /**
     * 上传用户头像
     *
     * @author: duyubo
     */
    @PostMapping(value = "/uploadPic")
    public RestResult<String> uploadPic(@RequestParam("uid") String uid, @RequestParam("file") MultipartFile file) throws IOException {
        return userService.uploadPicHandle(uid, file);
    }
}
