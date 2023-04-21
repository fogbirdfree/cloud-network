package com.dongruan.graduation.networkdiskssoserver.controller;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskssoserver.provider.SendSmsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendSmsController {
    @Autowired
    private SendSmsProvider sendSmsProvider;


    /**
     * 发送短信
     *
     * @author: duyubo
     */
    @PostMapping(value = "sendSms")
    public RestResult<String> sendSms(@RequestParam("phone") String phone) {
        return sendSmsProvider.sendSmsHandle(phone);
    }
}
