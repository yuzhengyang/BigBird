package com.yuzhyn.bigbird.app.system.config;

import com.yuzhyn.bigbird.app.system.interceptor.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc增加拦截器配置
 */
@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 实现跨域
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**");
    }
}