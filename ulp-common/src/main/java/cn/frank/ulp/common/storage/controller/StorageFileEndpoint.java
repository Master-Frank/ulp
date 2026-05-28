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
package cn.frank.ulp.common.storage.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import cn.frank.ulp.common.storage.Storage;
import cn.frank.ulp.support.constant.UlpConstants;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.MultipartConfigElement;
import static cn.frank.ulp.common.enums.ViewContentType.SVG;
import static cn.frank.ulp.common.storage.controller.StorageFileEndpoint.STORAGE_PATH;

/**
 * 存储配置
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "存储文件")
@RestController
@RequestMapping(value = STORAGE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class StorageFileEndpoint {

    /**
     * 存储API路径
     */
    public final static String STORAGE_PATH = UlpConstants.V1_API_PATH + "/storage";

    /**
     * 上传文件
     *
     * @return {@link ApiRestResult}
     */
    @Operation(summary = "上传文件")
    @PostMapping(value = "/upload")
    @PreAuthorize(value = "authenticated")
    public ApiRestResult<Object> uploadFile(@Schema(description = "文件") MultipartFile file) {
        try {
            if (storage == null) {
                return ApiRestResult.err().message("暂未开启存储服务，请联系管理员");
            }
            String fileName = new String(Objects.requireNonNull(file.getOriginalFilename())
                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String fileType = file.getContentType();
            List<String> allowedTypes = Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE,
                MimeTypeUtils.IMAGE_PNG_VALUE, SVG.getType(), "image/vnd.microsoft.icon");
            if (Objects.isNull(fileType) || !allowedTypes.contains(fileType)) {
                return ApiRestResult.err().message("文件类型不支持，请上传图片");
            }
            return ApiRestResult.ok(storage.upload(fileName, file.getInputStream()));
        } catch (Exception e) {
            log.error("Failed to upload storage file: {}", e.getMessage(), e);
            return ApiRestResult.err().message(e.getMessage());
        }
    }

    /**
     * 获取存储文件路径
     *
     * @return {@link ApiRestResult}
     */
    @Operation(summary = "获取存储文件路径")
    @GetMapping(value = "/get")
    @PreAuthorize(value = "authenticated")
    public ApiRestResult<Object> getUrl(@Schema(description = "文件路径") @RequestParam String path) {
        try {
            if (storage == null) {
                return ApiRestResult.err().message("暂未开启存储服务，请联系管理员");
            }
            return ApiRestResult.ok(storage.download(path));
        } catch (Exception e) {
            log.error("Failed to get storage file: {}", e.getMessage(), e);
            return ApiRestResult.err().message(e.getMessage());
        }
    }

    /**
     * 上传文件过大
     *
     * @return ApiRestResult<String>
     */
    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    public ApiRestResult<Object> maxUploadSizeExceededException() {
        return ApiRestResult.err()
            .message("文件大小不能超过:" + multipartConfigElement.getMaxFileSize() / 1024 / 1024 + "M");
    }

    /**
     * Storage
     */
    private final Storage                storage;

    private final MultipartConfigElement multipartConfigElement;
}
