/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.web.jackjson;

import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.support.web.useragent.UserAgent;

/**
 * 用户代理构建器Mixin类
 * 用于扩展用户代理构建器功能
 */
public class UserAgentBuilderMixin {
   
   /**
    * 用户代理列表
    */
   private List<UserAgent> userAgents = new ArrayList<>();

   /**
    * 添加用户代理
    * 
    * @param userAgent 用户代理
    * @return 用户代理构建器Mixin
    */
   public UserAgentBuilderMixin addUserAgent(UserAgent userAgent) {
      this.userAgents.add(userAgent);
      return this;
   }

   /**
    * 获取用户代理列表
    * 
    * @return 用户代理列表
    */
   public List<UserAgent> getUserAgents() {
      return this.userAgents;
   }
}
