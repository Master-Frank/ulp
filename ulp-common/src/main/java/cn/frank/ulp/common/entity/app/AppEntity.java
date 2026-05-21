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
package cn.frank.ulp.common.entity.app;

import java.util.List;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.app.AppProtocol;
import cn.frank.ulp.common.enums.app.AppType;
import cn.frank.ulp.common.enums.app.AuthorizationType;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

import static jakarta.persistence.FetchType.LAZY;

/**
 * 应用
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/11 21:07
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppEntity extends BaseEntity {

    /**
     * 应用名称
     */
    @Column(name = "name_")
    private String                          name;

    /**
     * 唯一CODE 不可修改
     */
    @Column(name = "code_")
    private String                          code;

    /**
     * 客户端ID
     */
    @Column(name = "client_id")
    private String                          clientId;
    /**
     * 客户端秘钥
     */
    @Column(name = "client_secret")
    private String                          clientSecret;

    /**
     * 模板
     */
    @Column(name = "template_")
    private String                          template;

    /**
     * 协议
     */
    @Column(name = "protocol_")
    private AppProtocol                     protocol;

    /**
     * 应用类型
     */
    @Column(name = "type_")
    private AppType                         type;

    /**
     * 应用图标
     */
    @Column(name = "icon_")
    private String                          icon;

    /**
     * SSO 发起登录URL
     */
    @Column(name = "init_login_url")
    private String                          initLoginUrl;

    /**
     * SSO 授权类型
     */
    @Column(name = "authorization_type")
    private AuthorizationType               authorizationType;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean                         enabled;

    /**
     * 是否配置
     */
    @Column(name = "is_configured")
    private Boolean                         configured;

    @ToString.Exclude
    @OneToMany(mappedBy = "app", fetch = LAZY)
    private List<AppGroupAssociationEntity> groups;
}
