package com.huasit.ssm.system.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

public class HTTPUtil {

    /**
     *
     */
    public static void addCookie(HttpServletResponse response, String key, String value) throws Exception {
        addCookie(response, key, value, "/", 60 * 60 * 24 * 365);
    }

    /**
     *
     */
    public static void addCookie(HttpServletResponse response, String key, String value, String path, int maxAge)
            throws Exception {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     *
     */
    public static String getCookies(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            String value = null;
            for (int i = 0; i < cookies.length; i++) {
                Cookie newCookie = cookies[i];
                if (newCookie.getName().equals(key) && newCookie.getValue() != null
                        && !newCookie.getValue().equals("")) {
                    value = newCookie.getValue();
                    break;
                }
            }
            if (value != null) {
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return value;
            }
        }
        return null;
    }

    /**
     *
     */
    public static void cleanCookies(HttpServletResponse response, String key) {
        cleanCookies(response, key, "/");
    }

    /**
     *
     */
    public static void cleanCookies(HttpServletResponse response, String key, String path) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath(path);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     *
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String[] headers = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        for (String header : headers) {
            String i = request.getHeader(header);
            if (i != null && !"".equals(i) && !"unkown".equalsIgnoreCase(i)) {
                ip = i.contains(",") ? i.split(",")[0] : i;
                break;
            }
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     *
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }
}