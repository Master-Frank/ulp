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

import java.lang.reflect.Constructor;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:40
 */
public class StorageFactory {

    private StorageFactory() {
    }

    /**
     * 获取实例化
     *
     * @param config {@link StorageConfig}
     * @return {@link Storage}
     */
    public static Storage getStorage(StorageConfig config) {
        try {
            Constructor<? extends Storage> constructor = config.getProvider().getStorage()
                .getDeclaredConstructor(StorageConfig.class);
            return constructor.newInstance(config);
        } catch (Exception e) {
            throw new StorageProviderException(e.getMessage(), e);
        }
    }
}
