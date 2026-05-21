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

import java.time.Duration;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.app.JwtBindingType;
import cn.frank.ulp.common.enums.app.JwtIdTokenSubjectType;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * APP Form 配置
 *
 * @author Frank Zhang
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "ulp_app_jwt_config")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppJwtConfigEntity extends BaseEntity {

    /**
     * APP ID
     */
    @Column(name = "app_id")
    private String                appId;

    /**
     * 业务系统中的JWT SSO地址，在单点登录时本系统将向该地址发送id_token信息，参数名为id_token，业务系统通过id_token与Public Key可获取业务系统中的用户信息，如果在业务系统（SP）发起登录，请求SP登录地址时如果携带service参数，系统会检验合法性，成功后会将浏览器重定向到该地址，并携带id_token身份令牌。
     */
    @Column(name = "redirect_url")
    private String                redirectUrl;

    /**
     * 业务系统中在JWT SSO成功后重定向的URL，一般用于跳转到二级菜单等，
     * 若设置了该URL，在JWT SSO时会以参数target_uri优先传递该值，
     * 若未设置该值，此时若SSO中有请求参数target_uri，则会按照请求参数传递该值。此项可选。
     */
    @Column(name = "target_link_url")
    private String                targetLinkUrl;

    /**
     * 跳转方式
     */
    @Column(name = "binding_type")
    private JwtBindingType        bindingType;

    /**
     * id_token sub 类型
     */
    @Column(name = "id_token_subject_type")
    private JwtIdTokenSubjectType idTokenSubjectType;

    /**
     * 令牌过期时间
     */
    @Column(name = "id_token_time_to_live")
    private Duration              idTokenTimeToLive;
}
