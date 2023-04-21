package com.dongruan.graduation.networkdiskssoserver.service;

import com.dongruan.graduation.networkdiskssoclient.rpc.Result;

/**
 * 应用服务接口
 * 
 * @author Joe
 */
public interface AppService {

	boolean exists(String appId);
	
	Result<Void> validate(String appId, String appSecret);
}
