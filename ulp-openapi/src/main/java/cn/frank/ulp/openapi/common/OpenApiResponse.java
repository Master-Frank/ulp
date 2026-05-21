/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.frank.ulp.openapi.constant.OpenApiStatus;
import cn.frank.ulp.support.trace.TraceUtils;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OpenApiResponse
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/25 03:40
 */
@Data
public class OpenApiResponse<T> {

    /**
     * code
     */
    @JsonProperty(value = "code")
    @Schema(description = "编码")
    private final String code;

    /**
     * msg
     */
    @JsonProperty(value = "msg")
    @Schema(description = "编码说明")
    private final String msg;

    /**
     * result
     */
    @JsonProperty(value = "result")
    @Schema(description = "返回结果")
    private T            result;

    /**
     * requestId
     */
    @JsonProperty(value = "requestId")
    @Schema(description = "请求 ID")
    private final String requestId;

    public OpenApiResponse(OpenApiStatus status) {
        this.code = status.getCode();
        this.msg = status.getDesc();
        this.requestId = TraceUtils.get();
    }

    public OpenApiResponse(String code, String message) {
        this.code = code;
        this.msg = message;
        this.requestId = TraceUtils.get();
    }

    public OpenApiResponse(OpenApiStatus status, T result) {
        this.code = status.getCode();
        this.msg = status.getDesc();
        this.result = result;
        this.requestId = TraceUtils.get();
    }

    public static OpenApiResponse<Void> success() {
        return new OpenApiResponse<>(OpenApiStatus.SUCCESS);
    }

    public static <T> OpenApiResponse<T> success(T result) {
        return new OpenApiResponse<>(OpenApiStatus.SUCCESS, result);
    }
}
