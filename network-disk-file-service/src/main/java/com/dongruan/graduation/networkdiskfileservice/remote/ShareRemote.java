package com.dongruan.graduation.networkdiskfileservice.remote;

import com.dongruan.graduation.networkdiskapi.service.ShareService;
import com.dongruan.graduation.networkdiskcommon.utils.BasicAuthConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "share-service", configuration = BasicAuthConfiguration.class)
public interface ShareRemote extends ShareService {
}
