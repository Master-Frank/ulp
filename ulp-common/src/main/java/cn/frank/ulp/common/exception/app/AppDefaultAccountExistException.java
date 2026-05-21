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
package cn.frank.ulp.common.exception.app;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * 默认应用账户已存在
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:49
 */
public class AppDefaultAccountExistException extends TopIamException {

    public AppDefaultAccountExistException() {
        super("app_default_account_exist", "默认应用账户已存在", DEFAULT_STATUS);
    }
}
