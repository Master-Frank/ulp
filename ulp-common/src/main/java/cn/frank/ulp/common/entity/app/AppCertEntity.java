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

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.app.AppCertUsingType;
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
 * @author Frank Zhang
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "ulp_app_cert")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppCertEntity extends BaseEntity {

    public static final String APP_ID_FIELD_NAME     = "appId";

    public static final String USING_TYPE_FIELD_NAME = "usingType";

    /**
     * 应用ID
     */
    @Column(name = "app_id")
    private String             appId;

    /**
     * 证书序列号
     */
    @Column(name = "serial_")
    private BigInteger         serial;

    /**
     * 主题信息
     */
    @Column(name = "subject_")
    private String             subject;

    /**
     * 签发者信息
     */
    @Column(name = "issuer_")
    private String             issuer;

    /**
     * 开始时间
     */
    @Column(name = "begin_date")
    private LocalDateTime      beginDate;

    /**
     * 结束时间
     */
    @Column(name = "end_date")
    private LocalDateTime      endDate;

    /**
     * 有效天数
     */
    @Column(name = "validity_")
    private Integer            validity;

    /**
     * 算法
     */
    @Column(name = "sign_algo")
    private String             signAlgo;

    /**
     * 私钥长度
     */
    @Column(name = "key_long")
    private Integer            keyLong;

    /**
     * 私钥
     */
    @Column(name = "private_key")
    private String             privateKey;

    /**
     * 公钥
     */
    @Column(name = "public_key")
    private String             publicKey;

    /**
     * 证书
     */
    @Column(name = "cert_")
    private String             cert;

    /**
     * 使用类型
     */
    @Column(name = "using_type")
    private AppCertUsingType   usingType;
}
