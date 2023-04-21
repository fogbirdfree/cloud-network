package com.dongruan.graduation.networkdiskssoserver.dto;

import com.dongruan.graduation.networkdiskssoclient.rpc.SsoUser;
import com.dongruan.graduation.networkdiskssoserver.common.AuthContent;

import java.io.Serializable;

public class AuthDto implements Serializable {

	private static final long serialVersionUID = 4587667812642196058L;

	private AuthContent authContent;
	private SsoUser user;

	public AuthDto(AuthContent authContent, SsoUser user) {
		this.authContent = authContent;
		this.user = user;
	}

	public AuthContent getAuthContent() {
		return authContent;
	}

	public void setAuthContent(AuthContent authContent) {
		this.authContent = authContent;
	}

	public SsoUser getUser() {
		return user;
	}

	public void setUser(SsoUser user) {
		this.user = user;
	}
}