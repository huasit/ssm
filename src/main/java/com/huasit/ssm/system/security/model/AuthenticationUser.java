package com.huasit.ssm.system.security.model;

import com.huasit.ssm.core.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthenticationUser implements UserDetails {

    /**
     *
     */
    private final User sources;

    /**
     *
     */
    private final String username;

    /**
     *
     */
    private final String password;

    /**
     *
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     *
     */
    public AuthenticationUser(User sources, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.sources = sources;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public User getSources() {
        return sources;
    }
}