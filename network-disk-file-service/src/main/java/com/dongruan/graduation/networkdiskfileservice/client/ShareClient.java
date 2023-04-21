package com.dongruan.graduation.networkdiskfileservice.client;

import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微服务调用
 */
@FeignClient(name="share-service")
public interface ShareClient {
	@PostMapping(value = "api/getVInfo")
	RestResult<String> getVInfo(@RequestParam("shareId") String shareId, @RequestParam("lockPassword") String lockPassword);
	@PostMapping(value = "api/addShareDownload")
	void addShareDownload(@RequestParam("shareId") String shareId);
}
