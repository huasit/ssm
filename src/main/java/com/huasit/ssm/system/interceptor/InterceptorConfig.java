package com.huasit.ssm.system.interceptor;

import com.huasit.ssm.system.locale.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     *
     */
    @Autowired
    Interceptor interceptor;

    /**
     *
     */
    @Autowired
    MessageSource messageSource;

    /**
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Locale.init(messageSource);
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}