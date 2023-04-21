package com.dongruan.graduation.networkdiskuser.service;

import com.dongruan.graduation.networkdiskcommon.param.ChangePwdParam;
import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: duyubo
 * @date: 2021年04月06日, 0006 14:39
 * @description:
 */
public interface UserInfoService {
    RestResult<String> modifyPassword(ChangePwdParam request);

    RestResult<UserInfoDTO> getUserInfo(@RequestParam("userId") String userId);
    /**
     * 加载用户头像数据处理
     *
     * @author: quhailong
     * @date: 2019/9/26
     */
    RestResult<String> loadImgHandle(String uid);

    /**
     * 上传用户头像数据处理
     *
     * @author: quhailong
     * @date: 2019/9/26
     */
    RestResult<String> uploadPicHandle(String uid, MultipartFile file) throws IOException;
}
