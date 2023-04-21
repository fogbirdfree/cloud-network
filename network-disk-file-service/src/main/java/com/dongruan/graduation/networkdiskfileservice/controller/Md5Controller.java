package com.dongruan.graduation.networkdiskfileservice.controller;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskfileservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Md5服务
 *
 * @author: duyubo
 */
@RestController
@RequestMapping(value = "/file")
public class Md5Controller {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private FileService fileService;

    /**
     * Md5校验服务
     *
     * @author: duyubo
     */
    @GetMapping(value = "md5Check")
    public RestResult<String> md5Check(String fid, String md5) {
        RestResult<String> result = new RestResult<>();
        redisTemplate.opsForValue().set("fileMd5:" + fid, md5, 259200);
        Integer count = fileService.checkMd5Whether(md5);
        if (count > 0) {
            result.success(null);
        } else {
            result.error();
        }
        return result;
    }
}
