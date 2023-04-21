package com.dongruan.graduation.networkdiskuser.service.impl;

import com.dongruan.graduation.networkdiskcommon.param.ChangePwdParam;
import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.IDUtils;
import com.dongruan.graduation.networkdiskcommon.utils.MD5Utils;
import com.dongruan.graduation.networkdiskcommon.utils.RSAUtils;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskuser.dao.UserInfoDao;
import com.dongruan.graduation.networkdiskuser.entity.UserInfoDO;
import com.dongruan.graduation.networkdiskuser.remote.FileRemote;
import com.dongruan.graduation.networkdiskuser.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * @author: duyubo
 * @date: 2021年04月06日, 0006 14:39
 * @description:
 */

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private FileRemote fileRemote;

    @Override
    public RestResult<String> modifyPassword(ChangePwdParam request) {
        RestResult<String> panResult = new RestResult<>();
        UserInfoDO userInfoDO = userInfoDao.getUserInfoByUserId(request.getUsername());
        if (userInfoDO == null) {
            panResult.error("用户信息不存在");
            return panResult;
        }
        String password = RSAUtils.decryptDataOnJava(request.getPassword(), request.getRsaKey());
        if (MD5Utils.verify(password, userInfoDO.getPassword())) {
            try {
                updateUser(userInfoDO, request.getNewPassword(), request.getRsaKey());
                panResult.success(null);
                return panResult;
            } catch (Exception e) {
                panResult.error("存入数据库发生错误");
                return panResult;
            }
        } else {
            panResult.error("密码错误");
            return panResult;
        }
    }

    @Override
    public RestResult<UserInfoDTO> getUserInfo(@RequestParam("userId") String userId) {
        RestResult<UserInfoDTO> panResult = new RestResult<>();
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        UserInfoDO userInfoDO = userInfoDao.getUserInfoByUserId(userId);
        if (userInfoDO != null) {
            BeanUtils.copyProperties(userInfoDO, userInfoDTO);
        }
        panResult.success(userInfoDTO);
        return panResult;
    }

    private void updateUser(UserInfoDO userInfoDO, String password, String RSAKey) throws Exception {
        try {
            String newPassword = RSAUtils.decryptDataOnJava(password, RSAKey);
            String salt = IDUtils.showNextId(new Random().nextInt(30)).toString().substring(0, 16);
            userInfoDO.setPassword(MD5Utils.generate(newPassword, salt));
            userInfoDO.setSalt(salt);
            userInfoDO.setUpdateTime(new Date());
            userInfoDao.updateUserInfo(userInfoDO);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public RestResult<String> loadImgHandle(String uid) {
        RestResult<String> panResult = new RestResult<>();
        UserInfoDO userInfoDO = userInfoDao.getUserInfoByUserId(uid);
        String picLocation = userInfoDO.getPicLocation();
        if (picLocation.contains("group")) {
            panResult.success("null");
            panResult.setRespData(picLocation);
        } else {
            panResult.success("null");
            panResult.setRespData("group1/M00/00/00/wKhdgF2RromAYwaYAAAJL2wXkdY418_big.jpg");
        }
        return panResult;
    }

    @Override
    public RestResult<String> uploadPicHandle(String uid, MultipartFile file) throws IOException {
        RestResult<String> panResult = new RestResult<>();
        String picLocation = fileRemote.upload(file).getRespData();
        UserInfoDO userInfoDO = userInfoDao.getUserInfoByUserId(uid);
        userInfoDO.setPicLocation(picLocation);
        userInfoDO.setUpdateTime(new Date());
        userInfoDao.updateUserInfo(userInfoDO);
        panResult.success(null);
        panResult.setDataCode("200");
        return panResult;
    }
}
