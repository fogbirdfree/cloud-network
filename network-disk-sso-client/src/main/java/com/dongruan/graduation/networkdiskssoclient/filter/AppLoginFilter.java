package com.dongruan.graduation.networkdiskssoclient.filter;

import com.dongruan.graduation.networkdiskssoclient.constant.SsoConstant;
import com.dongruan.graduation.networkdiskssoclient.session.SessionAccessToken;
import com.dongruan.graduation.networkdiskssoclient.util.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * APP单点登录Filter
 * 
 * @author duyubo
 */
public class AppLoginFilter extends LoginFilter {

	@Override
	public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SessionAccessToken sessionAccessToken = SessionUtils.getAccessToken(request);
		// 本地Session中已存在，且accessToken没过期或者refreshToken成功，直接返回
		if (sessionAccessToken != null && (!sessionAccessToken.isExpired()
				|| refreshToken(sessionAccessToken.getRefreshToken(), request))) {
			return true;
		}
		responseJson(response, SsoConstant.NO_LOGIN, "未登录或已超时");
		return false;
	}
}