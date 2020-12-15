package com.huasit.ssm.system.security.provider;

import com.huasit.ssm.system.security.model.UserWebAuthenticationDetails;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    /**
     *
     */
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new UserWebAuthenticationDetails(context);
    }
}
