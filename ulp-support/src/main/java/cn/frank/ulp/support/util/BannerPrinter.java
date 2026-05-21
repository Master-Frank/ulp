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
package cn.frank.ulp.support.util;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

/**
 * 横幅打印器
 * 用于打印应用启动时的横幅信息
 */
public class BannerPrinter implements Banner {
   
   /**
    * 打印横幅
    * 
    * @param environment 环境信息
    * @param sourceClass 源类
    * @param out 输出对象
    */
   public void printBanner(Environment environment, Class<?> sourceClass, java.io.PrintStream out) {
      out.println(PhoneUtils.decryptString("\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
   }
}
