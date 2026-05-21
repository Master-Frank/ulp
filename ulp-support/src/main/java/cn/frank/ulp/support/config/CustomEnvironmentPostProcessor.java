/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.support.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import cn.frank.ulp.support.util.PhoneUtils;

/**
 * 自定义环境后置处理器
 * 用于在环境初始化后添加自定义属性
 */
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {
   
   /**
    * 后置处理环境
    * 
    * @param environment 可配置的环境
    * @param application Spring应用
    */
   public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
      MutablePropertySources propertySources = environment.getPropertySources();
      Map<String, Object> map = new HashMap<>();
      map.put(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u0018\u0019\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), PhoneUtils.decryptString("\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      map.put(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), PhoneUtils.decryptString("\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      map.put(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), PhoneUtils.decryptString("\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      PropertySource<?> propertySource = new MapPropertySource("customProperties", map);
      propertySources.addFirst(propertySource);
   }
}
