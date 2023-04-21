package com.dongruan.graduation.networkdiskshareservice.remote;

import com.dongruan.graduation.networkdiskapi.service.UserService;
import com.dongruan.graduation.networkdiskcommon.utils.BasicAuthConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service", configuration = BasicAuthConfiguration.class)
public interface UserRemote extends UserService {
}
