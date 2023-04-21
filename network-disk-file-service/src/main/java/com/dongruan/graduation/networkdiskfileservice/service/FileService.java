package com.dongruan.graduation.networkdiskfileservice.service;


import com.dongruan.graduation.networkdiskcommon.param.QuickUploadFileParam;
import com.dongruan.graduation.networkdiskcommon.param.UploadFileParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface FileService {

    void uploadFile(UploadFileParam param) throws IOException;

    void quickUploadFile(QuickUploadFileParam param) throws UnsupportedEncodingException;

    Integer checkMd5Whether(String fileMd5);

    void download(String uid, String vid, HttpServletResponse res) throws IOException;

    void downloadShare(String lockPassword, String shareId, HttpServletResponse res) throws IOException;

    String upload(MultipartFile file) throws IOException;
}
