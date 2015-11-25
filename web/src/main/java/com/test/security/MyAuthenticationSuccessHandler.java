/**
 * 
 */
package com.test.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Nguyen Hai Vien
 *
 */
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private transient final Logger log = Logger.getLogger(getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        if (isAlwaysUseDefaultTargetUrl() || StringUtils.hasText(request.getParameter(getTargetUrlParameter()))) {
        	clearAuthenticationAttributes(request);
            String targetUrl = getDefaultTargetUrl();

            logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

    private boolean hasAuthority(Authentication authentication, String roleCode) {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>)authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(roleCode)) {
                return true;
            }
        }
        return false;
    }
}
