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
package cn.frank.ulp.application.form.model;

import java.io.Serial;
import java.util.List;

import org.springframework.util.CollectionUtils;

import cn.frank.ulp.application.AbstractProtocolConfig;
import cn.frank.ulp.common.entity.app.AppFormConfigEntity;
import cn.frank.ulp.common.enums.app.FormEncryptType;
import cn.frank.ulp.common.enums.app.FormSubmitType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Form 协议配置
 *
 * @author Frank Zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class FormProtocolConfig extends AbstractProtocolConfig {

    @Serial
    private static final long                    serialVersionUID = -3671812647788723766L;

    /**
     * 登录URL
     */
    private String                               loginUrl;

    /**
     * 登录名属性名称
     */
    private String                               usernameField;

    /**
     * 登录密码属性名称
     */
    private String                               passwordField;

    /**
     * 用户名加密类型
     */
    private FormEncryptType                      usernameEncryptType;

    /**
     * 用户名加密秘钥
     */
    private String                               usernameEncryptKey;

    /**
     * 登录密码加密类型
     */
    private FormEncryptType                      passwordEncryptType;

    /**
     * 登录密码加密秘钥
     */
    private String                               passwordEncryptKey;

    /**
     * 登录提交方式
     */
    private FormSubmitType                       submitType;

    /**
     * 登录其他信息
     */
    private List<AppFormConfigEntity.OtherField> otherField;

    /**
     * 是否配置
     */
    private Boolean                              configured;

    public List<AppFormConfigEntity.OtherField> getOtherField() {
        return CollectionUtils.isEmpty(otherField) ? List.of() : otherField;
    }
}
