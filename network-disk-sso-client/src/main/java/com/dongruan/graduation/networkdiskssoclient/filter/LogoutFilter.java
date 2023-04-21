package com.dongruan.graduation.networkdiskssoclient.filter;

import com.dongruan.graduation.networkdiskssoclient.constant.SsoConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 单点登出Filter
 * 
 * @author duyubo
 */
public class LogoutFilter extends ClientFilter {

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = getLogoutParam(request);
        if (accessToken != null) {
//            Cookie cookie = new Cookie("user", null);
//            if (cookie.getPath() != null) {
//                cookie.setPath("/");
//            }
//            cookie.setMaxAge(-1000);
//            response.addCookie(cookie);
//            for (Cookie coo : request.getCookies()) {
//                if ("user".equals(coo.getName())) {
//                    coo.setMaxAge(0);
//                    coo.setPath("/");
//                    response.addCookie(coo);
//                }
//            }
            destroySession(accessToken);
            return false;
        }
        return true;
    }
    
    protected String getLogoutParam(HttpServletRequest request) {
    	return request.getHeader(SsoConstant.LOGOUT_PARAMETER_NAME);
    }

    private void destroySession(String accessToken) {
        final HttpSession session = getSessionMappingStorage().removeSessionByMappingId(accessToken);
        if (session != null) {
            //使此session失效，但是返回后浏览器会立即生成一个新的session
            session.invalidate();
        }
    }
}