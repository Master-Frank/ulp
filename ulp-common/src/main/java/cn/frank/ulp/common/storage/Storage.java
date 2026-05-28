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

/**
 * 存储
 *
 * @author Frank Zhang
 */
public interface Storage {
    /**
     * 上传文件
     *
     * @param fileName {@link String}
     * @param inputStream {@link InputStream}
     * @return path
     * @throws Exception Exception
     */
    String upload(String fileName, InputStream inputStream) throws Exception;

    /**
     * 下载文件
     *
     * @param path {@link String}
     * @return path {@link String}
     * @throws Exception Exception
     */
    String download(String path) throws Exception;
}
