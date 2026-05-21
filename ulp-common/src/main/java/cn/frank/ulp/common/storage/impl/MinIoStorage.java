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
package cn.frank.ulp.common.storage.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.URL;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MimeTypeUtils;

import cn.frank.ulp.common.enums.ViewContentType;
import cn.frank.ulp.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.frank.ulp.common.storage.AbstractStorage;
import cn.frank.ulp.common.storage.StorageConfig;
import cn.frank.ulp.common.storage.StorageProviderException;
import cn.frank.ulp.common.storage.UploadException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.validation.constraints.NotEmpty;

/**
 * minio
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:32
 */
@Slf4j
public class MinIoStorage extends AbstractStorage {

    private final MinioClient minioClient;
    private final Config      minioConfig;

    public MinIoStorage(StorageConfig config) {
        super(config);
        try {
            minioConfig = (Config) this.config.getConfig();
            this.minioClient = MinioClient.builder().endpoint(minioConfig.getEndpoint())
                .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
            createBucket(this.minioClient, minioConfig);
        } catch (Exception e) {
            log.error("Create bucket exception: {}", e.getMessage(), e);
            throw new StorageProviderException("Create bucket exception", e);
        }
    }

    private void createBucket(MinioClient minioClient,
                              Config minioConfig) throws ServerException, InsufficientDataException,
                                                  ErrorResponseException, IOException,
                                                  NoSuchAlgorithmException, InvalidKeyException,
                                                  InvalidResponseException, XmlParserException,
                                                  InternalException {
        boolean found = minioClient
            .bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build());
        if (!found) {
            log.warn("{} does not exist", minioConfig.getBucket());
            minioClient
                .makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build());
        }
    }

    @Override
    public String upload(@NotNull String fileName,
                         InputStream inputStream) throws StorageProviderException {
        try {
            super.upload(fileName, inputStream);
            String key = this.minioConfig.getLocation() + SEPARATOR + getFileName(fileName);
            this.minioClient.putObject(PutObjectArgs.builder().bucket(this.minioConfig.getBucket())
                .object(key).contentType(ViewContentType.getContentType(key))
                .stream(inputStream, -1, 5 * 1024 * 1024).build());
            return this.minioConfig.getDomain() + SEPARATOR + this.minioConfig.getBucket()
                   + SEPARATOR
                   + URLEncoder.encode(key, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        } catch (Exception e) {
            log.error("Minio upload exception: {}", e.getMessage(), e);
            throw new UploadException(e);
        }
    }

    @Override
    public String download(String path) throws StorageProviderException {
        try {
            super.download(path);
            Map<String, String> headers = new HashMap<>(16);
            headers.put(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
            String downloadUrl = this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().bucket(minioConfig.getBucket()).object(path)
                    .method(Method.GET).expiry(EXPIRY_SECONDS).extraQueryParams(headers).build());
            return downloadUrl.replace(minioConfig.getEndpoint(), minioConfig.getDomain());
        } catch (Exception e) {
            log.error("minio download exception: {}", e.getMessage(), e);
            throw new StorageProviderException("minio download exception", e);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Config extends StorageConfig.Config {
        /**
         * AccessKey
         */
        @NotEmpty(message = "AccessKey不能为空")
        private String accessKey;
        /**
         * SecretKey
         */
        @JsonPropertyEncrypt
        @NotEmpty(message = "SecretKey不能为空")
        private String secretKey;
        /**
         * endpoint
         */
        @URL(message = "Endpoint格式不正确", regexp = URL_REGEXP)
        @NotEmpty(message = "Endpoint不能为空")
        private String endpoint;
        /**
         * bucket
         */
        @NotEmpty(message = "Bucket不能为空")
        private String bucket;
    }
}
