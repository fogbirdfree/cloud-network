package com.dongruan.graduation.networkdiskapi.service;

import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {
    @GetMapping(value = "user/getUserInfo/{userId}")
    RestResult<UserInfoDTO> getUserInfo(@PathVariable("userId") String userId);
}
