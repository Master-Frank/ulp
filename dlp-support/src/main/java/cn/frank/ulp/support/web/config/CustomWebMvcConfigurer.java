/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.frank.ulp.support.util.PhoneUtils;
import cn.frank.ulp.support.util.VersionUtils;
import cn.frank.ulp.support.web.converter.CustomEnumConverterFactory;
import cn.frank.ulp.support.web.filter.VersionHeaderFilter;
import cn.frank.ulp.support.web.useragent.DefaultUserAgentParser;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

/**
 * 自定义Web MVC配置器
 * 用于配置Web相关的组件和过滤器
 */
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
   
   /**
    * 当前版本过滤器注册Bean
    * 
    * @return 过滤器注册Bean
    */
   @Bean
   public FilterRegistrationBean<VersionHeaderFilter> currentVersionFilterRegistration() {
      FilterRegistrationBean<VersionHeaderFilter> filterRegistration = new FilterRegistrationBean<>();
      filterRegistration.setFilter(new VersionHeaderFilter());
      String[] urlPatterns = new String[1];
      urlPatterns[0] = VersionUtils.decryptString("\u0007z");
      filterRegistration.addUrlPatterns(urlPatterns);
      filterRegistration.setName(PhoneUtils.decryptString("\u0001g0`'|6D'`1{-|\u0004{.f'`"));
      filterRegistration.setOrder(-100);
      return filterRegistration;
   }

   /**
    * 用户代理解析器Bean
    * 
    * @return 用户代理解析器
    */
   @Bean
   public UserAgentParser userAgentParser() {
      return new DefaultUserAgentParser();
   }

   /**
    * 添加格式化器
    * 
    * @param registry 格式化器注册表
    */
   public void addFormatters(FormatterRegistry registry) {
      registry.addConverterFactory(new CustomEnumConverterFactory());
   }
}
