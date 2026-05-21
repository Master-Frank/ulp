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
package cn.frank.ulp.support.security.password.weak;

import java.util.List;

/**
 * 弱密码库接口
 * 定义弱密码库的基本操作方法
 */
public interface PasswordWeakLib {
   
   /**
    * 获取弱密码词列表
    *
    * @return 弱密码词列表
    */
   List<String> getWordList();

   /**
    * 检查单词是否存在于弱密码库中
    *
    * @param word 待检查的单词
    * @return 是否存在
    */
   Boolean wordExists(String word);
}