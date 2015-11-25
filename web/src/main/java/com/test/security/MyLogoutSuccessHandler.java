/**
 * 
 */
package com.test.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nguyen Hai Vien
 *
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    private transient final Logger logger = Logger.getLogger(getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private String logoutSuccessUrl = "/login.html?action=logout";

	/**
	 * @param logoutSuccessUrl the logoutSuccessUrl to set
	 */
	public void setLogoutSuccessUrl(String logoutSuccessUrl) {
		this.logoutSuccessUrl = logoutSuccessUrl;
	}
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
        String myLocalLogoutSuccessUrl = this.logoutSuccessUrl;

        SecurityContextHolder.clearContext();
        request.getSession(true); //Create new session
        redirectStrategy.sendRedirect(request, response, myLocalLogoutSuccessUrl);
	}
}
