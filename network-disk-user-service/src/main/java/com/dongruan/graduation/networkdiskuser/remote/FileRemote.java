package com.dongruan.graduation.networkdiskuser.remote;

import com.dongruan.graduation.networkdiskapi.service.FileService;
import com.dongruan.graduation.networkdiskcommon.utils.BasicAuthConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: duyubo
 * @date: 2021年04月06日, 0006 15:06
 * @description:
 */

@FeignClient(value = "file-service", configuration = BasicAuthConfiguration.class)
public interface FileRemote extends FileService {
}
