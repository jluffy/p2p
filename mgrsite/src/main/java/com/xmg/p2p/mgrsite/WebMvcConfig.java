package com.xmg.p2p.mgrsite;

import com.xmg.p2p.mgrsite.interceptor.CheckLoginInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class WebMvcConfig extends WebMvcConfigurerAdapter {
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLoginInterceptor()).addPathPatterns("/*").excludePathPatterns("/mgrlogin");
    }
}
