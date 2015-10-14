package com.test.utils;

import com.test.command.AbstractCommand;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.log4j.Logger;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.tuckey.web.filters.urlrewrite.utils.WildcardHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Convenience class for setting and retrieving cookies.
 */
public final class RequestUtil {
	private static final Logger log = Logger.getLogger(RequestUtil.class);

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private RequestUtil() {
    }

    /**
     * Convenience method to set common.a cookie
     *
     * @param response the current response
     * @param name the name of the cookie
     * @param value the value of the cookie
     * @param path the path to set it on
     */
    public static void setCookie(HttpServletResponse response, String name,
                                 String value, String path) {
        if (log.isDebugEnabled()) {
            log.debug("Setting cookie '" + name + "' on path '" + path + "'");
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(3600 * 24 * 30); // 30 days

        response.addCookie(cookie);
    }

    /**
     * Convenience method to get common.a cookie by name
     *
     * @param request the current request
     * @param name the name of the cookie to find
     *
     * @return the cookie (if found), null if not found
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;

        if (cookies == null) {
            return returnCookie;
        }

        for (final Cookie thisCookie : cookies) {
            if (thisCookie.getName().equals(name) && !"".equals(thisCookie.getValue())) {
                returnCookie = thisCookie;
                break;
            }
        }

        return returnCookie;
    }

    /**
     *
     * @param response
     * @param name
     * @param value
     * @param path
     */
    public static void setCookieDestroyOnClosed(HttpServletResponse response, String name,
                                 String value, String path) {
        if (log.isDebugEnabled()) {
            log.debug("Setting cookie '" + name + "' on path '" + path + "'");
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(-1); // auto destroy when browser is closed

        response.addCookie(cookie);
    }

    /**
     * Convenience method for deleting common.a cookie by name
     *
     * @param response the current web response
     * @param cookie the cookie to delete
     * @param path the path on which the cookie was set
     */
    public static void deleteCookie(HttpServletResponse response,
                                    Cookie cookie, String path) {
        if (cookie != null) {
            // Delete the cookie by setting its maximum age to zero
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }
    
    public static void deleteCookie(HttpServletResponse response, String cookieName, String path) {
    	deleteCookie(response, new Cookie(cookieName, null), path);
    }

    /**
     * Convenience method to get the application's URL based on request
     * variables.
     * 
     * @param request the current request
     * @return URL to application
     */
    public static String getAppURL(HttpServletRequest request) {
        if (request == null) return "";
        
        StringBuffer url = new StringBuffer();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = getRequestScheme(request);
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443 && port != 80))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getContextPath());
        return url.toString();
    }

    /**
     * Convenience method to get the server's URL based on request
     * variables not contain context path.
     *
     * @param request the current request
     * @return URL to server
     */
    public static String getServerURL(HttpServletRequest request) {
        if (request == null) return "";

        StringBuffer url = new StringBuffer();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = getRequestScheme(request);
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443 && port != 80))) {
            url.append(':');
            url.append(port);
        }
        return url.toString();
    }

    /**
     * init the SearchBean from request for pageing user list.
     * @param request HttpServletRequest
     * @param bean AbstractCommand
     */
    public static void initSearchBean(HttpServletRequest request, AbstractCommand bean) {
        if (bean != null){
            String sortExpression = request.getParameter(new ParamEncoder(bean.getTableId()).encodeParameterName(TableTagParameters.PARAMETER_SORT));
            String sortDirection = request.getParameter(new ParamEncoder(bean.getTableId()).encodeParameterName(TableTagParameters.PARAMETER_ORDER));
            String sPage = request.getParameter(new ParamEncoder(bean.getTableId()).encodeParameterName(TableTagParameters.PARAMETER_PAGE));
            Integer page = 1;
            if (StringUtils.isNotBlank(sPage)) {
                try {
                    page = Integer.valueOf(sPage);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            bean.setPage(page);
            bean.setFirstItem((bean.getPage() - 1) * bean.getMaxPageItems());
            bean.setSortExpression(sortExpression);
            bean.setSortDirection(sortDirection);
        }
    }
    
	public static String encodeUTF8(String s) {
		String result = null;

		try {
			result = URLEncoder.encode(s, "UTF-8");
		}

		// This exception should never occur.
		catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}

    public static String getClusterSessionId(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return getSessionWithoutSuffix(sessionId);
    }

    public static String getSessionWithoutSuffix(String sessionId) {
        int index = sessionId.indexOf(".");
        String partOfKey = index > 0 ? sessionId.substring(0, index) : sessionId;
        return partOfKey;
    }


    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        boolean found = false;
        if(StringUtils.isNotBlank(ip)) {
            StrTokenizer tokenizer = new StrTokenizer(ip, ",");
            while (tokenizer.hasNext()) {
                ip = tokenizer.nextToken().trim();
                if (isValidIpAddress(ip) && !isPrivateIpAddress(ip)) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static boolean isValidIpAddress(final String ip) {
        return InetAddressUtils.isIPv4Address(ip) || InetAddressUtils.isIPv6Address(ip);
    }

    public static boolean isPrivateIpAddress(final String ipAddress) {
        return isIPv4Private(ipAddress) || isInetAddressPrivate(ipAddress);
    }

    public static boolean isInetAddressPrivate(final String ipAddress) {
        try {
            InetAddress ia = null;
            InetAddress ad = InetAddress.getByName(ipAddress);
            byte[] ip = ad.getAddress();
            ia = InetAddress.getByAddress(ip);

            return ia.isSiteLocalAddress();
        } catch (UnknownHostException e) {
        }
        return false;
    }

    public static boolean isIPv4Private(String ip) {
        long longIp = ipV4ToLong(ip);
        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255")) ||
                (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255")) ||
                longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
    }

    public static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16) +
                (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    public static boolean isRequestFromHttps(HttpServletRequest request) {
        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        return forwardedProto != null && forwardedProto.equalsIgnoreCase("https");
    }

    public static String getRequestScheme(HttpServletRequest request) {
        if(!request.getScheme().equalsIgnoreCase("https") && isRequestFromHttps(request)) {
            return "https";
        }
        return request.getScheme();
    }

    public static boolean isMatchWildcard(String pattern, String data) {
        if(StringUtils.isNotBlank(data)) {
            WildcardHelper wh = new WildcardHelper();
            String rawPattern = StringUtils.trim(pattern).toLowerCase();
            int[] compiledPattern = wh.compilePattern(rawPattern);
            return wh.match(new HashMap(), data.toLowerCase(), compiledPattern);
        }
        return false;
    }

    public static boolean isMatchWildcards(String[] patterns, String data) {
        if(patterns != null) {
            for(String pattern : patterns) {
                if(isMatchWildcard(pattern, data)) {
                    return true;
                }
            }
        }
        return false;
    }
}
