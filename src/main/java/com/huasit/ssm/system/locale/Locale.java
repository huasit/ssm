package com.huasit.ssm.system.locale;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

public class Locale {

    /**
     *
     */
    private java.util.Locale locale;

    /**
     *
     */
    public Locale(java.util.Locale locale) {
        this.locale = locale;
    }

    /**
     *
     */
    private static MessageSource messageSource;

    /**
     *
     */
    public static void init(MessageSource messageSource) {
        Locale.messageSource = messageSource;
    }

    /**
     *
     */
    public static Locale getInstance() {
        return new Locale(java.util.Locale.getDefault());
    }

    /**
     *
     */
    public static Locale getInstance(HttpServletRequest request) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        return new Locale(localeResolver == null ? java.util.Locale.getDefault() : localeResolver.resolveLocale(request));
    }

    /**
     *
     */
    public String getMessage(String key, Object... objects) {
        return messageSource.getMessage(key, objects, locale);
    }
}
