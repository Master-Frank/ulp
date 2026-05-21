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

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import cn.frank.ulp.common.enums.app.FormEncryptType;
import cn.frank.ulp.common.enums.app.FormSubmitType;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * APP Form 配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/13 22:31
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app_form_config")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppFormConfigEntity extends BaseEntity {

    /**
     * APP ID
     */
    @Column(name = "app_id")
    private String           appId;

    /**
     * 登录URL
     */
    @Column(name = "login_url")
    private String           loginUrl;

    /**
     * 登录名属性名称
     */
    @Column(name = "username_field")
    private String           usernameField;

    /**
     * 登录密码属性名称
     */
    @Column(name = "password_field")
    private String           passwordField;

    /**
     * 登录密码加密类型
     */
    @Column(name = "password_encrypt_type")
    private FormEncryptType  passwordEncryptType;

    /**
     * 登录密码加密秘钥
     */
    @Column(name = "password_encrypt_key")
    private String           passwordEncryptKey;

    /**
     * 用户名加密类型
     */
    @Column(name = "username_encrypt_type")
    private FormEncryptType  usernameEncryptType;

    /**
     * 用户名加密秘钥
     */
    @Column(name = "username_encrypt_key")
    private String           usernameEncryptKey;

    /**
     * 登录提交方式
     */
    @Column(name = "submit_type")
    private FormSubmitType   submitType;

    /**
     * 登录其他信息
     */
    @Column(name = "other_field")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<OtherField> otherField;

    @Data
    @Schema(description = "表单其他信息")
    public static class OtherField implements Serializable {

        private String fieldName;

        private String fieldValue;
    }
}
