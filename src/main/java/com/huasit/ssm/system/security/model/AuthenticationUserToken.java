package com.huasit.ssm.system.security.model;

import java.io.Serializable;
import java.util.Date;

public class AuthenticationUserToken implements Serializable {

    /**
     *
     */
    private final String token;

    /**
     *
     */
    private final Date expireDate;

    /**
     *
     */
    private final AuthenticationUser user;

    /**
     *
     */
    public AuthenticationUserToken(String token, Date expireDate, AuthenticationUser user) {
        this.token = token;
        this.expireDate = expireDate;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public AuthenticationUser getUser() {
        return user;
    }
}