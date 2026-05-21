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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.enums.app.AppProtocol;
import cn.frank.ulp.common.enums.app.AppType;
import cn.frank.ulp.common.enums.app.AuthorizationType;
import cn.frank.ulp.support.security.userdetails.Application;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用账户po
 *
 * @author Frank Zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppPO extends AppEntity {

    /**
     * 应用分组
     */
    private List<Application.ApplicationGroup> group;

    public AppPO(String id, String name, String code, String clientId, String clientSecret,
                 String template, AppProtocol protocol, AppType type, String icon,
                 String initLoginUrl, AuthorizationType authorizationType, Boolean enabled,
                 String groupIds) {
        super.setId(id);
        super.setName(name);
        super.setCode(code);
        super.setClientId(clientId);
        super.setClientSecret(clientSecret);
        super.setTemplate(template);
        super.setProtocol(protocol);
        super.setType(type);
        super.setIcon(icon);
        super.setInitLoginUrl(initLoginUrl);
        super.setAuthorizationType(authorizationType);
        super.setEnabled(enabled);
        if (StringUtils.isNotBlank(groupIds)) {
            this.group = new ArrayList<>();
            for (String groupId : groupIds.split(",")) {
                this.group.add(new Application.ApplicationGroup(groupId, null, null));
            }
        }
    }
}
