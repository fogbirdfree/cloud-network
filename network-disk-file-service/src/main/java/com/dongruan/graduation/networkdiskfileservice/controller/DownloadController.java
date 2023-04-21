package com.dongruan.graduation.networkdiskfileservice.controller;

import com.dongruan.graduation.networkdiskfileservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 下载文件
 *
 * @author: duyubo
 */
@RestController
@RequestMapping(value = "/file")
public class DownloadController {
    @Autowired
    private FileService fileService;

    /**
     * 下载文件
     *
     * @author: duyubo
     */
    @GetMapping(value = "download")
    public void download(String uid, String vids, HttpServletResponse res) throws IOException {
        fileService.download(uid, vids, res);
    }

    /**
     * 下载分享文件
     *
     * @author: duyubo
     */
    @GetMapping(value = "downloadShare")
    public void downloadShare(String lockPassword, String shareId, HttpServletResponse res) throws IOException {
        fileService.downloadShare(lockPassword, shareId, res);
    }
}
