package com.dongruan.graduation.networkdiskssoserver.controller;

import com.dongruan.graduation.networkdiskcommon.param.UserRegisterParam;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskssoserver.service.RegisterService;
import com.dongruan.graduation.networkdiskssoserver.validator.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 注册服务
 *
 * @author: quhailong
 * @date: 2019/9/25
 */
@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    /**
     * 用户名查重
     *
     * @author: 杜宇博
     */

    @GetMapping(value = "/checkUsername")
    public RestResult<String> checkUserName(@RequestParam("userName") String userName) {
        return registerService.checkUserNameHandle(userName);
    }

    /**
     * 手机号查重
     *
     * @author: duyubo
     */
    @GetMapping(value = "checkPhone")
    public RestResult<String> checkPhone(@RequestParam("phone") @Phone String phone) {
        return registerService.checkPhoneHandle(phone);
    }

    /**
     * 用户注册
     *
     * @author: duyubo
     */
    @PostMapping(value = "register")
    public RestResult<String> register(@RequestBody UserRegisterParam request) throws Exception {
        return registerService.userRegisterHandle(request);
    }

}
