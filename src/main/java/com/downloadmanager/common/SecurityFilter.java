/*
 *  
 * Created on Oct 16, 2016
 *
 */
package com.downloadmanager.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author SumeetS
 *
 */
public class SecurityFilter implements Filter{
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    private static final List<String> URI_LIST = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	URI_LIST.add("/swagger");
	URI_LIST.add("/v2/api-docs");
	URI_LIST.add("/download");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpSession session = ((HttpServletRequest) request).getSession(false);
	String method = ((HttpServletRequest) request).getMethod();
	String uri = ((HttpServletRequest) request).getRequestURI();

	if (!HttpMethod.OPTIONS.toString().equals(method) && !containsURI(uri) && getLoggedInUser(response, session)) {
	    return;
	}
	HttpServletResponse res = (HttpServletResponse) response;
	HttpServletRequest req = (HttpServletRequest) request;
	String clientOrigin = req.getHeader("origin");
	res.setHeader("Access-Control-Allow-Origin", clientOrigin); // "http://172.19.129.105:3000"
	res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, PATCH");
	res.setHeader("Access-Control-Max-Age", "3600");
	res.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
	res.setHeader("Access-Control-Allow-Credentials", "true");
	res.setHeader("X-FRAME-OPTIONS", "ALLOW-FROM " + clientOrigin);
	chain.doFilter(request, response);
    }

    private boolean getLoggedInUser(ServletResponse response, HttpSession session) throws IOException {
	if (session == null) {
	    LOGGER.warn("User not logged in");
	    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
	    return true;
	} 

	SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
	Authentication auth = securityContext.getAuthentication();

	if (!(auth instanceof AnonymousAuthenticationToken)) {
	    Object principal = auth.getPrincipal();
	    if (principal instanceof UserDetails) {
		return false;
	    }
	}
	return true;
    }

    private boolean containsURI(String uri) {
	for (String str : URI_LIST) {
	    if (uri.contains(str)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void destroy() {
    }
}
