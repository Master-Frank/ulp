/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.setting;

/**
 * 存储提供商设置
 *
 * @author Frank Zhang
 */
public final class StorageProviderSettingConstants {

    /**
     * 存储提供商前缀
     */
    public static final String STORAGE_BEAN_NAME      = "storage";

    /**
     * 存储提供商前缀
     */
    public static final String STORAGE_SETTING_PREFIX = "storage.";
    /**
     * 存储提供商KEY
     */
    public static final String STORAGE_PROVIDER_KEY   = STORAGE_SETTING_PREFIX + "provider";
}
