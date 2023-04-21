package com.dongruan.graduation.networkdiskapi.service;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    @PostMapping(value = "file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestResult<String> upload(@RequestPart MultipartFile file) throws IOException;
}
