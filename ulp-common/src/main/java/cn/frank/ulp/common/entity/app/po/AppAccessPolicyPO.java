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

import cn.frank.ulp.common.entity.app.AppAccessPolicyEntity;
import cn.frank.ulp.common.enums.app.AppPolicySubjectType;
import cn.frank.ulp.common.enums.app.AppProtocol;
import cn.frank.ulp.common.enums.app.AppType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用授权策略po
 *
 * @author Frank Zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppAccessPolicyPO extends AppAccessPolicyEntity {
    /**
     * 应用名称
     */
    private String      appName;

    /**
     * 应用图标
     */
    private String      appIcon;

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

    /**
     * 授权主体
     */
    private String      subjectName;

    public AppAccessPolicyPO(String id, String appId, String subjectId,
                             AppPolicySubjectType subjectType, Boolean enabled,
                             LocalDateTime createTime, String subjectName, String appName,
                             String appIcon, AppType appType, String appTemplate,
                             AppProtocol appProtocol) {

        super.setId(id);
        super.setAppId(appId);
        super.setSubjectId(subjectId);
        super.setSubjectType(subjectType);
        super.setSubjectType(subjectType);
        super.setEnabled(enabled);
        super.setCreateTime(createTime);
        this.subjectName = subjectName;
        this.appName = appName;
        this.appIcon = appIcon;
        this.appType = appType;
        this.appTemplate = appTemplate;
        this.appProtocol = appProtocol;
    }
}
