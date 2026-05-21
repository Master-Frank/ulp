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
package cn.frank.ulp.common.storage.impl;

import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import cn.frank.ulp.common.storage.Storage;
import cn.frank.ulp.common.storage.StorageProviderException;

/**
 * 本地存储配置
 *
 * @author Frank Zhang
 */
public class NoneStorage implements Storage {

    public NoneStorage() {
    }

    @Override
    public String upload(@NotNull String fileName,
                         InputStream inputStream) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }

    @Override
    public String download(String path) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }
}
