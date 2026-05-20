/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import cn.frank.ulp.support.security.util.SecurityUtils;

/**
 * 自定义审计员感知器
 * 用于获取当前审计员（用户ID）
 */
public final class CustomAuditorAware implements AuditorAware<String> {
   
   /**
    * 获取当前审计员
    * 
    * @return 当前审计员的Optional包装
    */
   @NonNull
   public Optional<String> getCurrentAuditor() {
      return Optional.ofNullable(SecurityUtils.getCurrentUserId());
   }
}
