/*
 * ulp-support - United Login Platform
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

/**
 * 版本信息类
 * 提供系统版本信息的工具类
 */
public class VersionInfo {

    /**
    * 序列化版本UID
    */
    public static final long SERIAL_VERSION_UID = (long) getVersion().hashCode();

    // 合成字段
    private static final int D                  = 0;

    // 合成字段
    private static final int b                  = 0;

    // 合成字段
    private static final int SYNTHETIC_FIELD    = 2;

    /**
     * 获取版本信息
     *
     * @return 版本字符串
     */
    public static String getVersion() {
        return PhoneUtils.decryptString("p<r<r?\u0001W");
    }
}
