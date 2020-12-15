package com.huasit.ssm.system.security.model;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class UserWebAuthenticationDetails extends WebAuthenticationDetails {

    /**
     *
     */
    private final String name;

    /**
     *
     */
    public UserWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.name = request.getParameter("name");
    }

    public String getName() {
        return name;
    }
}
