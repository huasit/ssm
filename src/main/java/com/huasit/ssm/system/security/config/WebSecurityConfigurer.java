package com.huasit.ssm.system.security.config;

import com.huasit.ssm.system.security.filter.TokenAuthenticationFilter;
import com.huasit.ssm.system.security.handler.AuthenticationHandler;
import com.huasit.ssm.system.security.provider.AuthenticationProvider;
import com.huasit.ssm.system.security.provider.UserAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    /**
     *
     */
    @Autowired
    private AuthenticationHandler athenticationHandler;

    /**
     *
     */
    @Autowired
    private AuthenticationProvider authenticationProvider;

    /**
     *
     */
    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    /**
     *
     */
    @Autowired
    private UserAuthenticationDetailsSource authenticationDetailsSource;

    /**
     *
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login/")
                .successHandler(athenticationHandler)
                .failureHandler(athenticationHandler)
                .authenticationDetailsSource(authenticationDetailsSource)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout/")
                .logoutSuccessHandler(athenticationHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(athenticationHandler)
                .and()
                .csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().cacheControl();
    }
}