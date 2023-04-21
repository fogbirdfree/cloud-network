package com.dongruan.graduation.networkdiskuser.controller;

import com.dongruan.graduation.networkdiskssoclient.constant.SsoConstant;
import com.dongruan.graduation.networkdiskssoclient.rpc.SsoUser;
import com.dongruan.graduation.networkdiskssoclient.util.SessionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class IndexController {

	@Value("${server.port}")
	private Integer serverPort;
	@Value("${sso.server.url}")
	private String serverUrl;

	/**
	 * 初始页
	 * @param request
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@GetMapping(value = "/")
	public String index(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		SsoUser user = SessionUtils.getUser(request);
		// 登录用户名
		model.addAttribute("userName", user.getUsername());
		model.addAttribute("uid", user.getId());
		model.addAttribute("logoutUrl", serverUrl + SsoConstant.LOGOUT_URL + "?" + SsoConstant.REDIRECT_URI + "="
				+ URLEncoder.encode(getLocalUrl(request), "utf-8"));
		return "index";
	}
	

    /**
     * 获取当前应用访问路径
     *
     * @param request
     * @return
     */
    private String getLocalUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        url.append(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            url.append(":").append(request.getServerPort());
        }
        url.append(request.getContextPath());
        return url.toString();
    }
}
