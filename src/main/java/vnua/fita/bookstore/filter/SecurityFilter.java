package vnua.fita.bookstore.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnua.fita.bookstore.bean.User;
import vnua.fita.bookstore.config.SecurityConfig;
import vnua.fita.bookstore.util.MyUtil;

@WebFilter(filterName = "securityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request=(HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse) res;
		String servletPathFull=request.getServletPath();
		String servletPath=MyUtil.getServletPath(servletPathFull);

		if(!SecurityConfig.checkDenyUrlPattern(servletPath)) {
			chain.doFilter(request, response);
			return;
		}
		
		User loginedUser=MyUtil.getLoginedUser(request.getSession());
		boolean isPermisstion=false;
		if(loginedUser!=null) {
			byte role=loginedUser.getRole();
			isPermisstion=SecurityConfig.checkPermission(role, servletPath);
		}
		if(!isPermisstion) {
			response.sendRedirect(request.getContextPath()+"/");
			return;
		}
		
		chain.doFilter(request, response);
	}

}
