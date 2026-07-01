package com.travel.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors( InterceptorRegistry registry ) {

    registry.addInterceptor( new SaInterceptor( handle -> StpUtil.checkLogin() ) ).addPathPatterns( "/**" ).excludePathPatterns(
        "/api/users/login",
        "/api/users/register",
        "/doc.html", "/webjars/**", "/v3/api-docs/**", "/favicon.ico"
    );
    
  }

}
