package com.expensecalculator.interceptors;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.expensecalculator.dto.User;
import com.expensecalculator.enums.Role;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationInterceptor implements Interceptor{
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String requestURI = request.getRequestURI();
        
        String token = getJWTToken(request.getCookies());
        if (token == null && requestURI.contains("/user")) {
        	System.out.println(requestURI+" "+(token==null));
            invocation.invoke();
            return null;
        }
        else if(token ==null) {
        	System.out.println(requestURI+" "+(token==null));
        	response.sendRedirect(request.getContextPath() + "/user/showlogin");
        	return null;
        }
        
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256("secret"))
                                .build()
                                .verify(token);
            
            int userId = jwt.getClaim("userId").asInt();
            int userRoleId = jwt.getClaim("userRoleId").asInt();
            String userIdParam = request.getParameter("userId");
            
            if(userIdParam!=null && !userIdParam.equals(userId+"")) {
            	System.out.println(requestURI+" "+(token==null));
            	removeAuthorization(request, response);
                return null;
            }
            else if (!hasAccess(userRoleId, requestURI)) {
            	System.out.println(requestURI+" "+(token==null));
            	removeAuthorization(request, response);
                return null;
            }
            
            System.out.println(requestURI+" "+(token==null));

            invocation.invoke();
            return null;
        } catch (JWTDecodeException exception) {
        	System.out.println(requestURI+" "+(token==null));
            removeAuthorization(request, response);
            return null;
        }
    }
	
	private String getJWTToken(Cookie[] cookies) {
		String token=null;
		if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
		
		return token;
	}
	
	private void removeAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/user/showlogin");
	}

      
	
	private boolean hasAccess(int userRoleId, String requestURI) {
		if (userRoleId == 1) {
			return requestURI.contains("/category") || 	requestURI.contains("/home") || requestURI.contains("/admin")	;
		} else if (userRoleId == 2) {
			return requestURI.contains("/category") || requestURI.contains("/home") || requestURI.contains("/guser") || requestURI.contains("/transaction")	;
		}
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
