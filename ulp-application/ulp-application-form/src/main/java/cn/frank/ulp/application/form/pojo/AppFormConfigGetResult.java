/*
 * ulp-application-form - United Login Platform
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
package cn.frank.ulp.application.form.pojo;

import java.io.Serializable;
import java.util.List;

import cn.frank.ulp.common.entity.app.AppFormConfigEntity;
import cn.frank.ulp.common.enums.app.FormEncryptType;
import cn.frank.ulp.common.enums.app.FormSubmitType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Form 配置返回
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "Form 配置返回响应")
public class AppFormConfigGetResult implements Serializable {
    /**
     * 应用id
     */
    @Schema(description = "应用id")
    private String                               appId;

    /**
     * SSO 登录链接
     */
    @Schema(description = "SSO 登录链接")
    private String                               initLoginUrl;

    /**
     * 登录URL
     */
    @Schema(description = "登录URL")
    private String                               loginUrl;

    /**
     * 登录名属性名称
     */
    @Schema(description = "登录名属性名称")
    private String                               usernameField;

    /**
     * 登录密码属性名称
     */
    @Schema(description = "登录密码属性名称")
    private String                               passwordField;

    /**
     * 登录密码加密类型
     */
    @Schema(name = "登录密码加密类型")
    private FormEncryptType                      passwordEncryptType;

    /**
     * 登录密码加密秘钥
     */
    @Schema(name = "登录密码加密秘钥")
    private String                               passwordEncryptKey;

    /**
     * 用户名加密类型
     */
    @Schema(name = "用户名加密类型")
    private FormEncryptType                      usernameEncryptType;

    /**
     * 用户名加密秘钥
     */
    @Schema(name = "用户名加密秘钥")
    private String                               usernameEncryptKey;

    /**
     * 登录提交方式
     */
    @Schema(description = "登录提交方式")
    private FormSubmitType                       submitType;

    /**
     * 登录其他信息
     */
    @Schema(description = "登录其他信息")
    private List<AppFormConfigEntity.OtherField> otherField;

    /**
     * 协议端点
     */
    @Schema(description = "协议端点")
    private AppFormProtocolEndpoint              protocolEndpoint;
}
