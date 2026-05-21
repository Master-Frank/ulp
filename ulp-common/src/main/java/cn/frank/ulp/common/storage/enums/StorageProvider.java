/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.storage.enums;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.common.storage.Storage;
import cn.frank.ulp.common.storage.impl.*;
import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * 存储提供商
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/19
 */
public enum StorageProvider implements Serializable {

                                                     /**
                                                      * 阿里云
                                                      */
                                                     ALIYUN_OSS("aliyun_oss", "阿里云OSS",
                                                                AliYunOssStorage.class),
                                                     /**
                                                      * 腾讯云
                                                      */
                                                     TENCENT_COS("tencent_cos", "腾讯云COS",
                                                                 TencentCosStorage.class),
                                                     /**
                                                      * 七牛
                                                      */
                                                     QINIU_KODO("qiniu_kodo", "七牛云Kodo",
                                                                QiNiuKodoStorage.class),
                                                     /**
                                                      * minio
                                                      */
                                                     MINIO("minio", "minio", MinIoStorage.class),
                                                     /**
                                                      * S3
                                                      */
                                                     S3("s3", "s3", S3Storage.class);

    /**
     * code
     */
    @JsonValue
    private final String                   code;
    /**
     * desc
     */
    private final String                   desc;
    /**
     * Storage
     */
    private final Class<? extends Storage> storage;

    StorageProvider(String code, String desc, Class<? extends Storage> storage) {
        this.code = code;
        this.desc = desc;
        this.storage = storage;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Class<? extends Storage> getStorage() {
        return storage;
    }

    @EnumConvert
    public static StorageProvider getType(String code) {
        StorageProvider[] values = values();
        for (StorageProvider status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该平台");
    }

    @Override
    public String toString() {
        return this.code;
    }
}
