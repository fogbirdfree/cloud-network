package com.dongruan.graduation.networkdiskssoclient.filter;

import com.dongruan.graduation.networkdiskssoclient.listener.LogoutListener;
import com.dongruan.graduation.networkdiskssoclient.session.SessionMappingStorage;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter基类
 * 
 * @author duyubo
 */
public abstract class ClientFilter extends ParamFilter implements Filter {
	
	private SessionMappingStorage sessionMappingStorage;
    
	public abstract boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response)
			throws IOException;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException {
	}

	@Override
	public void destroy() {
	}
	
	protected SessionMappingStorage getSessionMappingStorage() {
		if (sessionMappingStorage == null) {
            sessionMappingStorage = LogoutListener.getSessionMappingStorage();
        }
		return sessionMappingStorage;
	}
}