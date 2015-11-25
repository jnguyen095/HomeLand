package com.test.security;

import com.test.utils.RequestUtil;

import javax.servlet.http.*;

public class MyRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletResponse response = null;

	public MyRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		HttpSession session = super.getSession();
		processSessionCookie(session);
		return session;
	}

	public HttpSession getSession(boolean create) {
		HttpSession session = super.getSession(create);
		processSessionCookie(session);
		return session;
	}

	private void processSessionCookie(HttpSession session) {
		if (null == response || null == session) {
			// No response or session object attached, skip the pre processing
			return;
		}
		// cookieOverWritten - Flag to filter multiple "Set-Cookie" headers
		Object cookieOverWritten = getAttribute("COOKIE_OVERWRITTEN_FLAG");
		if (null == cookieOverWritten && isSecure()
				&& isRequestedSessionIdFromCookie()) {
			// Might have created the cookie in SSL protocol and tomcat will
			// loose the session
			// if there is change in protocol from HTTPS to HTTP. To avoid this,
			// trick the browser
			// using the HTTP and HTTPS session cookie.
            Cookie cookie = new Cookie("JSESSIONID", RequestUtil.getSessionWithoutSuffix(session.getId()));
			cookie.setMaxAge(-1); // Life of the browser or timeout
			String contextPath = getContextPath();
			if ((contextPath != null) && (contextPath.length() > 0)) {
				cookie.setPath(contextPath);
			} else {
				cookie.setPath("/");
			}
			response.addCookie(cookie); // Adding an "Set-Cookie" header to the
										// response
			setAttribute("COOKIE_OVERWRITTEN_FLAG", "true");// To avoid multiple
															// "Set-Cookie"
															// header
		}
	}
}
