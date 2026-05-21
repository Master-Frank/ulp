/*
 * ulp-application-core - United Login Platform
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
package cn.frank.ulp.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.app.AppAccountEntity;
import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.enums.app.AppProtocol;
import cn.frank.ulp.common.enums.app.AppType;
import cn.frank.ulp.common.enums.app.AuthorizationType;

/**
 * 应用接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/20 23:20
 */
public interface ApplicationService {

    /**
     * 获取应用标志
     *
     * @return {@link String}
     */
    String getCode();

    /**
     * 获取应用名称
     *
     * @return {@link String}
     */
    String getName();

    /**
     * 获取应用描述
     *
     * @return {@link String}
     */
    String getDescription();

    /**
     * 获取应用类型
     *
     * @return {@link String}
     */
    AppType getType();

    /**
     * 获取应用协议
     *
     * @return {@link AppProtocol}
     */
    AppProtocol getProtocol();

    /**
     * 获取表单Schema
     *
     * @return {@link Object}
     */
    default Object getFormSchema() {
        return null;
    }

    /**
     * 获取base64图标
     *
     * @return {@link String}
     */
    String getBase64Icon();

    /**
     * 创建应用
     *
     * @param name     {@link String} 名称
     * @param icon     {@link String} 图标
     * @param remark   {@link String} 备注
     * @return {@link String} 应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    default String create(String name, String icon, String remark) {
        return create(name, icon, remark, new ArrayList<>());
    }

    /**
     * 创建应用
     *
     * @param name     {@link String} 名称
     * @param icon     {@link String} 图标
     * @param remark   {@link String} 备注
     * @param groups {@link String} 应用分组
     * @return {@link String} 应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    String create(String name, String icon, String remark, List<String> groups);

    /**
     * 删除应用
     *
     * @param appId {@link String} 应用ID
     */
    void delete(String appId);

    /**
     * 更新应用配置
     *
     * @param appId {@link String}
     * @param config {@link Map}
     */
    @Transactional(rollbackFor = Exception.class)
    void saveConfig(String appId, Map<String, Object> config);

    /**
     * 获取配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    Object getConfig(String appId);

    /**
     * 获取默认应用用户信息
     *
     * @param appId {@link String}
     * @param userId {@link String}
     * @return {@link AppAccountEntity}
     */
    AppAccount getDefaultAppAccount(String appId, String userId);

    /**
     * 创建应用
     *
     * @param name {@link String}
     * @param icon  {@link String}
     * @param remark  {@link String}
     * @param authorizationType {@link AuthorizationType}
     * @return {@link AppEntity}
     */
    AppEntity createApp(String name, String icon, String remark,
                        AuthorizationType authorizationType);

    /**
     * 创建应用
     *
     * @param name {@link String}
     * @param icon  {@link String}
     * @param remark  {@link String}
     * @param groups {@link String} 应用分组
     * @param authorizationType {@link AuthorizationType}
     * @return {@link AppEntity}
     */
    AppEntity createApp(String name, String icon, String remark, List<String> groups,
                        AuthorizationType authorizationType);
}
