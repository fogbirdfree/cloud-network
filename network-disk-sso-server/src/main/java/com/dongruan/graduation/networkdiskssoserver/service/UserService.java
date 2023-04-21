package com.dongruan.graduation.networkdiskssoserver.service;

import com.dongruan.graduation.networkdiskcommon.param.ChangePwdParam;
import com.dongruan.graduation.networkdiskcommon.param.ModifyPassParam;
import com.dongruan.graduation.networkdiskcommon.response.UserInfoDTO;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import com.dongruan.graduation.networkdiskssoclient.rpc.Result;
import com.dongruan.graduation.networkdiskssoclient.rpc.SsoUser;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务接口
 * 
 * @author duyubo
 */
public interface UserService {
	
	/**
	 * 登录
	 * 
	 * @param username
	 *            登录名
	 * @param password
	 *            密码
	 * @return
	 */
	Result<SsoUser> login(String username, String password, String RSAKey);

	RestResult<String> forgetPassword(ModifyPassParam request);

}
