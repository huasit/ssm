package com.huasit.ssm.system.security.provider;

import com.huasit.ssm.system.security.model.AuthenticationUser;
import com.huasit.ssm.system.security.model.UserWebAuthenticationDetails;
import com.huasit.ssm.system.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    /**
     *
     */
    @Autowired
    UserDetailsService userDetailsService;

    /**
     *
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    /**
     *
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserWebAuthenticationDetails details = (UserWebAuthenticationDetails) authentication.getDetails();
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        AuthenticationUser user = userDetailsService.loadUserByUsernameAndName(username, details.getName());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new BadCredentialsException(username);
        }
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }
}