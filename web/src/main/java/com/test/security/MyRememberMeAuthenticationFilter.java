package com.test.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("deprecation")
public class MyRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.onSuccessfulAuthentication(request, response, authentication);
    }

}
