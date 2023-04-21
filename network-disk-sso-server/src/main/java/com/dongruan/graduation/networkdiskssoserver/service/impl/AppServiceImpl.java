package com.dongruan.graduation.networkdiskssoserver.service.impl;

import com.dongruan.graduation.networkdiskssoclient.rpc.Result;
import com.dongruan.graduation.networkdiskssoserver.model.App;
import com.dongruan.graduation.networkdiskssoserver.service.AppService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service("appService")
public class AppServiceImpl implements AppService {

	private static List<App> appList;

	static {
		appList = new ArrayList<>();
		appList.add(new App("服务端1", "server1", "123456"));
		appList.add(new App("客户端1", "demo1", "123456"));
	}

	@Override
	public boolean exists(String appId) {
		return appList.stream().anyMatch(app -> app.getAppId().equals(appId));
	}

	@Override
	public Result<Void> validate(String appId, String appSecret) {
		for (App app : appList) {
			if (app.getAppId().equals(appId)) {
				if (app.getAppSecret().equals(appSecret)) {
					return Result.success();
				}
				else {
					return Result.createError("appSecret有误");
				}
			}
		}
		return Result.createError("appId不存在");
	}
}
