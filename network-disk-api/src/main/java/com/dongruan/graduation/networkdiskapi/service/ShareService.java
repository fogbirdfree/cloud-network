package com.dongruan.graduation.networkdiskapi.service;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ShareService {

    @PostMapping(value = "share/addShareDownload")
    void addShareDownload(@RequestParam("shareId") String shareId);

    @GetMapping(value = "share/getVInfo")
    RestResult<String> getUid(@RequestParam("shareId") String shareId, @RequestParam("lockPassword") String lockPassword);
}
