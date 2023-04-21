package com.dongruan.graduation.networkdiskshareservice.remote;

import com.dongruan.graduation.networkdiskapi.service.CoreService;
import com.dongruan.graduation.networkdiskcommon.utils.BasicAuthConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "core-service", configuration = BasicAuthConfiguration.class)
public interface CoreRemote extends CoreService {
}
