package com.dongruan.graduation.networkdiskssoserver.controller;

import com.dongruan.graduation.networkdiskssoclient.constant.SsoConstant;
import com.dongruan.graduation.networkdiskssoserver.constant.AppConstant;
import com.dongruan.graduation.networkdiskssoserver.session.AccessTokenManager;
import com.dongruan.graduation.networkdiskssoserver.session.TicketGrantingTicketManager;
import com.dongruan.graduation.networkdiskssoserver.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 单点登出
 * 
 * @author duyubo
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

	@Autowired
	private AccessTokenManager accessTokenManager;
    @Autowired
    private TicketGrantingTicketManager ticketGrantingTicketManager;

	/**
	 * 登出
	 * 
	 * @param redirectUri
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping
	public String logout(
			@RequestParam(value = SsoConstant.REDIRECT_URI, required = true) String redirectUri,
	        HttpServletRequest request, HttpServletResponse response) {
		String tgt = CookieUtils.getCookie(request, AppConstant.TGC);
		if (!StringUtils.isEmpty(tgt)) {
			// 删除登录凭证
		    ticketGrantingTicketManager.remove(tgt);
		    // 删除凭证Cookie
		    CookieUtils.removeCookie(AppConstant.TGC, "/", response);
			CookieUtils.removeCookie("user", "/", response);
			// 删除所有tgt对应的调用凭证，并通知客户端登出注销本地session
		    accessTokenManager.remove(tgt);
		}
        return "redirect:" + redirectUri;
	}
}