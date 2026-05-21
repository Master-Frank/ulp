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

import java.io.Serial;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.UlpException;

/**
 * 存储服务异常
 *
 * @author Frank Zhang
 */
public class StorageProviderException extends UlpException {
    @Serial
    private static final long serialVersionUID = 6249098979022610064L;

    public StorageProviderException(String msg, Throwable t) {
        super(msg, t);
    }

    public StorageProviderException(String msg) {
        super(msg);
    }

    public StorageProviderException(String msg, HttpStatus status) {
        super(msg, status);
    }

    public StorageProviderException(String error, String description, HttpStatus status) {
        super(error, description, status);
    }

    public StorageProviderException(Throwable cause, String error, String description,
                                    HttpStatus status) {
        super(cause, error, description, status);
    }
}
