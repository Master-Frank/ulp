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
package cn.frank.ulp.common.exception;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.UlpException;

/**
 * @author Frank Zhang
 */
public class UserGroupNotExistException extends UlpException {

    public UserGroupNotExistException() {
        this("用户组不存在");
    }

    public UserGroupNotExistException(String message) {
        super("user_group_not_exist", message, HttpStatus.BAD_REQUEST);
    }
}
