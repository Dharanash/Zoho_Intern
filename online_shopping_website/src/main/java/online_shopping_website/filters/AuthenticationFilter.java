package online_shopping_website.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import online_shopping_website.enums.Role;
import online_shopping_website.model.User;

public class AuthenticationFilter extends HttpFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String requestURI = req.getRequestURI();
		User user = (User) req.getSession().getAttribute("auth");
		
		if (user == null && !requestURI.endsWith("/user/login") && !requestURI.endsWith("/user/register")) {
			res.sendRedirect(req.getContextPath() + "/user/login");
		} else if (user != null && (requestURI.endsWith("/user/login") || requestURI.endsWith("/user/register"))) {
			
			res.sendRedirect(req.getContextPath() + "/user/home");
		}else if(user != null && !hasAccess(user.role, requestURI)) {
			res.sendRedirect(req.getContextPath() + "/user/home");
		}
		else {
			
			chain.doFilter(request, response);
		}
	}

	private boolean hasAccess(Role userRole, String requestURI) {
		if (userRole == Role.Admin) {
			return requestURI.contains("/user") || requestURI.contains("/inventory") || requestURI.endsWith("/orders/getallorders") || requestURI.contains("/orders/getreceivedorders")
					|| requestURI.contains("/orders/dispatchreceivedorder")	|| requestURI.contains("/orders/cancelreceivedorder")	;
		} else if (userRole == Role.Manager) {
			return requestURI.endsWith("/user/home") || requestURI.endsWith("/user/login") || requestURI.endsWith("/user/logout") || requestURI.endsWith("/user/register") || requestURI.endsWith("/user/updateprofile")|| requestURI.endsWith("/user/getprofile")
					|| requestURI.contains("/inventory") || requestURI.contains("/orders/getreceivedorders")
					|| requestURI.contains("/orders/dispatchreceivedorder")	|| requestURI.contains("/orders/cancelreceivedorder")	;
		} else if(userRole == Role.Customer){
			return requestURI.endsWith("/user/home") || requestURI.endsWith("/user/login") || requestURI.endsWith("/user/logout") || requestURI.endsWith("/user/register") || requestURI.endsWith("/user/updateprofile")|| requestURI.endsWith("/user/getprofile")
					|| requestURI.contains("/wallet") || requestURI.contains("/cart") || requestURI.endsWith("/inventory/get") 
					|| requestURI.contains("/orders/getdeliverydetails") || requestURI.contains("/orders/adddeliverydetail")	|| requestURI.contains("/orders/placeorder")
					|| requestURI.contains("/orders/getuserorder") || requestURI.contains("/orders/cancelorder")	|| requestURI.contains("/orders/updatedeliverydetails")	;
		}
		return false;
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
