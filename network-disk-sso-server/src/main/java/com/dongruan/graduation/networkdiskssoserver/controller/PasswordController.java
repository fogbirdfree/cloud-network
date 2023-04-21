package com.dongruan.graduation.networkdiskssoserver.controller;

import com.dongruan.graduation.networkdiskcommon.param.ChangePwdParam;
import com.dongruan.graduation.networkdiskcommon.param.ModifyPassParam;
import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskssoserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码服务
 *
 * @author: duyubo
 */
@RestController
public class PasswordController {
    @Autowired
    private UserService userService;

    /**
     * 忘记密码
     *
     * @author: duyubo
     */
    @PostMapping(value = "forgetPassword")
    public RestResult<String> forgetPhoneSend(@RequestBody ModifyPassParam param) {
        return userService.forgetPassword(param);
    }

}
