package com.dongruan.graduation.networkdiskssoserver.service;

import com.dongruan.graduation.networkdiskcommon.param.UserRegisterParam;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RegisterService {
    /**
     * 用户名查重数据处理
     *
     * @author: quhailong
     * @date: 2019/9/25
     */
    RestResult<String> checkUserNameHandle(String username);

    /**
     * 手机号查重数据处理
     *
     * @author: quhailong
     * @date: 2019/9/25
     */
    RestResult<String> checkPhoneHandle(String phone);

    /**
     * 用户注册数据处理
     *
     * @author: quhailong
     * @date: 2019/9/25
     */
    RestResult<String> userRegisterHandle(UserRegisterParam request) throws Exception;

}
