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
package cn.frank.ulp.common.entity.app.po;

import java.time.LocalDateTime;

import cn.frank.ulp.common.entity.app.AppAccountEntity;
import cn.frank.ulp.common.enums.app.AppProtocol;
import cn.frank.ulp.common.enums.app.AppType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用账户po
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/10 22:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppAccountPO extends AppAccountEntity {

    /**
     * 用户名称
     */
    private String      username;

    /**
     * 应用名称
     */
    private String      appName;

    /**
     * 模板
     */
    private String      appTemplate;

    /**
     * 协议
     */
    private AppProtocol appProtocol;

    /**
     * 应用类型
     */
    private AppType     appType;

    public AppAccountPO(String id, String appId, String userId, String account,
                        LocalDateTime createTime, Boolean defaulted, String username,
                        String appName, AppType appType, String appTemplate,
                        AppProtocol appProtocol) {

        super.setId(id);
        super.setAppId(appId);
        super.setUserId(userId);
        super.setAccount(account);
        super.setCreateTime(createTime);
        super.setDefaulted(defaulted);
        this.username = username;
        this.appName = appName;
        this.appType = appType;
        this.appTemplate = appTemplate;
        this.appProtocol = appProtocol;
    }

    public AppAccountPO(String id, String appId, String userId, String username, String appName) {
        super.setId(id);
        super.setAppId(appId);
        super.setUserId(userId);
        this.username = username;
        this.appName = appName;
    }
}
