package com.huasit.ssm.system.security.service;

import com.huasit.ssm.core.permission.entity.Permission;
import com.huasit.ssm.core.role.entity.Role;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.core.user.entity.UserToken;
import com.huasit.ssm.core.user.service.UserService;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import com.huasit.ssm.system.security.model.AuthenticationUserToken;
import com.huasit.ssm.system.util.HTTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    /**
     *
     */
    @Autowired
    private UserService userService;

    /**
     *
     */
    public AuthenticationUser loadUserByToken(String token) throws UsernameNotFoundException {
        User user = this.userService.loadLoginUserByToken(token);
        if (user == null) {
            return null;
        }
        return this.parseUser(user);
    }

    /**
     *
     */
    @Override
    public AuthenticationUser loadUserByUsername(String username) {
        User user = this.userService.loadLoginUserByUsername(username);
        if (user == null) {
            return null;
        }
        return this.parseUser(user);
    }

    /**
     *
     */
    public AuthenticationUser loadUserByUsernameAndName(String username, String name) {
        User user = this.userService.loadLoginUserByUsername(username);
        if (user == null) {
            return null;
        }
        if(user.getType() == User.UserType.STUDENT && !user.getName().equals(name)) {
            return null;
        }
        return this.parseUser(user);
    }

    /**
     *
     */
    public AuthenticationUserToken generateToken(AuthenticationUser user, HttpServletRequest request) {
        UserToken userToken = this.userService.createLoginUserToken(user.getUsername(), HTTPUtil.getIp(request), HTTPUtil.getUserAgent(request));
        return this.parseUserToken(userToken);
    }

    /**
     *
     */
    private AuthenticationUser parseUser(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getSign()));
            }
        }
        if (user.getPermissions() != null) {
            for (Permission permission : user.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getSign()));
            }
        }
        if (user.getId() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPER"));
        }
        return new AuthenticationUser(user, user.getUsername(), user.getPassword(), authorities);
    }

    /**
     *
     */
    private AuthenticationUserToken parseUserToken(UserToken userToken) {
        return new AuthenticationUserToken(userToken.getToken(), userToken.getExpireDate(), this.parseUser(userToken.getUser()));
    }
}