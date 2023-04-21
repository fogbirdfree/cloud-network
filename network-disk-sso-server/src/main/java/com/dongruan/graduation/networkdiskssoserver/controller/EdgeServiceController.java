package com.dongruan.graduation.networkdiskssoserver.controller;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskssoserver.service.EdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EdgeServiceController {
    @Autowired
    private EdgeService edgeService;

    /**
     * 生成公钥
     *
     * @author: duyubo
     */
    @GetMapping(value = "getPublicKey")
    public RestResult<String> getPublicKey() throws Exception {
        RestResult<String> result = new RestResult<>();
        result.setRespMap(edgeService.getPublicKey());
        result.setRespData("sucess");
        return result;
    }

    /**
     * 检查密码格式
     *
     * @author: duyubo
     */
    @PostMapping(value = "regCheckPwd")
    public RestResult<String> regCheckPwd(@RequestParam("password") String password, @RequestParam("RSAKey") String RSAKey) {
        RestResult<String> result = new RestResult<>();
        try {
            edgeService.regCheckPwd(password, RSAKey);
        } catch (Exception e) {
            result.error(e.getMessage());
            return result;
        }
        result.success(null);
        return result;
    }

}
