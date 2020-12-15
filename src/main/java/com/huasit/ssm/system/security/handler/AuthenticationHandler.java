package com.huasit.ssm.system.security.handler;

import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import com.huasit.ssm.system.security.model.AuthenticationUserToken;
import com.huasit.ssm.system.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler, AuthenticationFailureHandler, AuthenticationEntryPoint, AccessDeniedHandler {

    /**
     *
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     *
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        AuthenticationUser user = (AuthenticationUser) authentication.getPrincipal();
        AuthenticationUserToken token = this.userDetailsService.generateToken(user, request);
        Response.success("name", user.getSources().getName(), "token", token.getToken(), "expire_date", token.getExpireDate()).write(response);
    }

    /**
     *
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.clearContext();
    }

    /**
     *
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        if (e instanceof UsernameNotFoundException) {
            Response.error(SystemError.USERNAME_NOTFOUND, request).write(response);
        } else if (e instanceof BadCredentialsException) {
            Response.error(SystemError.BAD_CREDENTIALS, request).write(response);
        } else {
            Response.error(SystemError.ACCESS_DENIED, request).write(response);
        }
    }

    /**
     *
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        Response.error(SystemError.CREDENTIALS_EXPIRED, request).write(response);
    }

    /**
     *
     */
    @Override
    @ExceptionHandler(value = AccessDeniedException.class)
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        Response.error(SystemError.ACCESS_DENIED, request).write(response);
    }
}
