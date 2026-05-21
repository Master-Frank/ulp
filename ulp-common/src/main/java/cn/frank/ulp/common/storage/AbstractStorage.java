/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.storage;

import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frank Zhang
 */
public class AbstractStorage implements Storage {
    protected StorageConfig    config;
    public static final String SEPARATOR      = "/";
    public static final String JOINER         = "-";
    public static final int    EXPIRY_SECONDS = 3600;
    /**
     * url 正则
     */
    public static final String URL_REGEXP     = "^https?://[\\w.-]+(:\\d+)?$";

    public AbstractStorage(StorageConfig config) {
        this.config = config;
    }

    /**
     * 判断域名是否为https
     *
     * @param url {@link String}
     * @return {@link Boolean}
     */
    public boolean getUrlSecure(String url) {
        return "https:".equals(url.split("//")[0]);
    }

    public String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "";
        }
        return UUID.randomUUID().toString().replace(JOINER, "").toLowerCase() + JOINER + fileName;
    }

    @Override
    public String upload(@NotNull String fileName,
                         InputStream inputStream) throws StorageProviderException {
        return null;
    }

    @Override
    public String download(String path) throws StorageProviderException {
        return null;
    }
}
