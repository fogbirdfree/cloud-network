package com.dongruan.graduation.networkdiskssoserver.controller;

import com.dongruan.graduation.networkdiskssoclient.constant.Oauth2Constant;
import com.dongruan.graduation.networkdiskssoclient.constant.SsoConstant;
import com.dongruan.graduation.networkdiskssoclient.rpc.Result;
import com.dongruan.graduation.networkdiskssoclient.rpc.SsoUser;
import com.dongruan.graduation.networkdiskssoserver.constant.AppConstant;
import com.dongruan.graduation.networkdiskssoserver.service.AppService;
import com.dongruan.graduation.networkdiskssoserver.service.UserService;
import com.dongruan.graduation.networkdiskssoserver.session.CodeManager;
import com.dongruan.graduation.networkdiskssoserver.session.TicketGrantingTicketManager;
import com.dongruan.graduation.networkdiskssoserver.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 单点登录管理
 * 
 * @author duyubo
 */
@Controller
public class LoginController{

	@Autowired
	private CodeManager codeManager;
	@Autowired
	private TicketGrantingTicketManager ticketGrantingTicketManager;
	@Autowired
	private UserService userService;
	@Autowired
	private AppService appService;

	/**
	 * 登录页
	 * 
	 * @param redirectUri
	 * @param appId
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/login")
	public String login(
			@RequestParam(value = SsoConstant.REDIRECT_URI) String redirectUri,
			@RequestParam(value = Oauth2Constant.APP_ID) String appId,
			HttpServletRequest request) throws UnsupportedEncodingException {
		String tgt = CookieUtils.getCookie(request, AppConstant.TGC);
		if (StringUtils.isEmpty(tgt) || ticketGrantingTicketManager.get(tgt) == null) {
			return goLoginPath(redirectUri, appId, request);
		}
		return generateCodeAndRedirect(redirectUri, tgt);
	}
	
	/**
	 * 登录提交
	 * 
	 * @param redirectUri
	 * @param appId
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping(value = "/login")
	public String login(
			@RequestParam(value = SsoConstant.REDIRECT_URI) String redirectUri,
			@RequestParam(value = Oauth2Constant.APP_ID) String appId,
			@RequestParam String username, 
			@RequestParam String password,
			@RequestParam String RSAKey,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		if(!appService.exists(appId)) {
			request.setAttribute("errorMessage", "非法应用");
			return goLoginPath(redirectUri, appId, request);
		}
		
		Result<SsoUser> result = userService.login(username, password, RSAKey);
		if (!result.isSuccess()) {
			request.setAttribute("errorMessage", result.getMessage());
			return goLoginPath(redirectUri, appId, request);
		}

		String tgt = CookieUtils.getCookie(request, AppConstant.TGC);
		if (StringUtils.isEmpty(tgt) || ticketGrantingTicketManager.get(tgt) == null) {
			tgt = ticketGrantingTicketManager.generate(result.getData());

			// TGT存cookie，和Cas登录保存cookie中名称一致为：TGC
			CookieUtils.addCookie(AppConstant.TGC, tgt, "/", request, response);
		}
		return generateCodeAndRedirect(redirectUri, tgt);
	}

	/**
	 * 设置request的redirectUri和appId参数，跳转到登录页
	 * 
	 * @param redirectUri
	 * @param request
	 * @return
	 */
	private String goLoginPath(String redirectUri, String appId, HttpServletRequest request) {
		request.setAttribute(SsoConstant.REDIRECT_URI, redirectUri);
		request.setAttribute(Oauth2Constant.APP_ID, appId);
		return AppConstant.LOGIN_PATH;
	}
	
	/**
	 * 生成授权码，跳转到redirectUri
	 * 
	 * @param redirectUri
	 * @param tgt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String generateCodeAndRedirect(String redirectUri, String tgt) throws UnsupportedEncodingException {
		// 生成授权码
		String code = codeManager.generate(tgt, true, redirectUri);
		return "redirect:" + authRedirectUri(redirectUri, code);
	}

	/**
	 * 将授权码拼接到回调redirectUri中
	 * 
	 * @param redirectUri
	 * @param code
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String authRedirectUri(String redirectUri, String code) throws UnsupportedEncodingException {
		StringBuilder sbf = new StringBuilder(redirectUri);
		if (redirectUri.indexOf("?") > -1) {
			sbf.append("&");
		}
		else {
			sbf.append("?");
		}
		sbf.append(Oauth2Constant.AUTH_CODE).append("=").append(code);
		return URLDecoder.decode(sbf.toString(), "utf-8");
	}

}