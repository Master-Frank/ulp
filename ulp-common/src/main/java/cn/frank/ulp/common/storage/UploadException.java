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

/**
 * 存储服务异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/19 22:53
 */
public class UploadException extends StorageProviderException {
    @Serial
    private static final long serialVersionUID = 6249098979022610064L;

    public UploadException(Throwable cause) {
        super(cause, "Upload fail", cause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
