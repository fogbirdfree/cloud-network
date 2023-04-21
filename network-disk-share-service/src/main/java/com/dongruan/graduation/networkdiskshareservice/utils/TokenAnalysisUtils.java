package com.dongruan.graduation.networkdiskshareservice.utils;

import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.JSONUtils;
import com.dongruan.graduation.networkdiskcommon.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

/**
 * Token分析工具类
 *
 * @author: quhailong
 * @date: 2019/9/27
 */
@Component
public class TokenAnalysisUtils {
    public UserInfoDTO tokenAnalysis(String token) {
        Claims claims = JWTUtils.parseJWT(token, "nimadetou".getBytes());
        String subject = claims.getSubject();
        UserInfoDTO userInfoDTO = JSONUtils.parseObject(subject, UserInfoDTO.class);
        return userInfoDTO;
    }
}
