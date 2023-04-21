package com.dongruan.graduation.networkdiskssoserver.service.impl;

import com.dongruan.graduation.networkdiskcommon.param.UserRegisterParam;
import com.dongruan.graduation.networkdiskcommon.utils.HttpClientUtils;
import com.dongruan.graduation.networkdiskcommon.utils.IDUtils;
import com.dongruan.graduation.networkdiskcommon.utils.MD5Utils;
import com.dongruan.graduation.networkdiskcommon.utils.RSAUtils;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskssoserver.dao.CapacityDao;
import com.dongruan.graduation.networkdiskssoserver.dao.UserInfoDao;
import com.dongruan.graduation.networkdiskssoserver.entity.CapacityDO;
import com.dongruan.graduation.networkdiskssoserver.entity.UserInfoDO;
import com.dongruan.graduation.networkdiskssoserver.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CapacityDao capacityDao;

    private static Pattern p1 = Pattern.compile("^[a-zA-Z0-9\u4E00-\u9FA5_]+$");
    private static Pattern p2 = Pattern.compile("^[0-9]+$");
    private static Pattern p3 = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

    @Override
    public RestResult<String> checkUserNameHandle(String username) {
        RestResult<String> panResult = new RestResult<>();
        if (p1.matcher(username).matches() && !p2.matcher(username).matches()) {
            UserInfoDO userInfoDO = userInfoDao.getUserInfoByUserNameOrPhone(username, null);
            if (userInfoDO != null) {
                panResult.error("用户名已经被使用了，请更换");
                return panResult;
            }
            panResult.success(null);
            return panResult;
        } else {
            panResult.error("用户名仅支持中英文、数字和下划线,且不能为纯数字");
            return panResult;
        }
    }

    @Override
    public RestResult<String> checkPhoneHandle(String phone) {
        RestResult<String> panResult = new RestResult<>();
        if (p3.matcher(phone).matches()) {
            UserInfoDO userInfoDO = userInfoDao.getUserInfoByUserNameOrPhone(null, phone);
            if (userInfoDO != null) {
                panResult.setRespCode(144);
                panResult.setRespData(null);
                return panResult;
            }
            panResult.success(null);
            return panResult;
        } else {
            panResult.error("手机号码格式不正确");
            return panResult;
        }
    }

    @Override
    public RestResult<String> userRegisterHandle(UserRegisterParam request) throws Exception {
        RestResult<String> panResult = new RestResult<>();
        String code = redisTemplate.opsForValue().get(request.getPhone());
        if (code != null && Objects.equals(code, request.getVerifyCode())) {
            UserInfoDO userInfoDO = new UserInfoDO();
            String salt = IDUtils.showNextId(new Random().nextInt(30)).toString().substring(0, 16);
            userInfoDO.setPassword(MD5Utils.generate(RSAUtils.decryptDataOnJava(request.getPassword(), request.getRsaKey()), salt));
            userInfoDO.setSalt(salt);
            userInfoDO.setPhone(request.getPhone());
            userInfoDO.setUserName(request.getUsername());
            String userId = IDUtils.showNextId(new Random().nextInt(30)).toString();
            userInfoDO.setUserId(userId);
            userInfoDO.setCreateTime(new Date());
            userInfoDO.setUpdateTime(new Date());
            userInfoDO.setPicLocation("/");
            userInfoDao.saveUserInfo(userInfoDO);
            UserInfoDO user = userInfoDao.getUserInfoById(userId);
            saveCapacity(user.getId().toString());
            panResult.success(null);
            return panResult;
        } else {
            panResult.error("验证码错误");
            return panResult;
        }
    }

    public Integer saveCapacity(String uid) {
        CapacityDO capacityDO = new CapacityDO();
        capacityDO.setUserId(uid);
        capacityDO.setTotalCapacity(5368709120L);
        capacityDO.setUsedCapacity(0L);
        return capacityDao.saveCapacity(capacityDO);
    }
}
