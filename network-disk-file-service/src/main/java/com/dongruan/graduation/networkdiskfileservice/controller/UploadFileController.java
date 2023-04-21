package com.dongruan.graduation.networkdiskfileservice.controller;

import com.dongruan.graduation.networkdiskcommon.param.QuickUploadFileParam;
import com.dongruan.graduation.networkdiskcommon.param.UploadFileParam;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskfileservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 上传文件服务
 *
 * @author: duyubo
 */
@RestController
@RequestMapping(value = "/file")
public class UploadFileController {
    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     *
     * @author: duyubo
     */
    @PostMapping(value = "uploadFile")
    public RestResult<String> uploadFile(UploadFileParam param) throws IOException {
        RestResult<String> result = new RestResult<>();
        fileService.uploadFile(param);
        result.success(null);
        return result;
    }

    /**
     * 秒传文件
     *
     * @author: duyubo
     */
    @PostMapping(value = "quickUploadFile")
    public RestResult<String> quickUploadFile(QuickUploadFileParam param) throws UnsupportedEncodingException {
        RestResult<String> result = new RestResult<>();
        fileService.quickUploadFile(param);
        result.success(null);
        return result;
    }

    /**
     * 上传文件(内部调用)
     *
     * @author: duyubo
     */
    @PostMapping(value = "upload")
    public RestResult<String> upload(MultipartFile file) throws IOException {
        RestResult<String> result = new RestResult<>();
        result.success(fileService.upload(file));
        return result;
    }

}
