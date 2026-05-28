/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletResponse;

/**
 * HTTP 响应辅助工具.
 */
public class HttpResponseUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public HttpResponseUtils() {
    }

    public static void flushResponse(HttpServletResponse response, String body) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(body)) {
                writer.write(body);
            } else {
                writer.write("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void flushResponseJson(HttpServletResponse response, int status, Object data) {
        flushResponseJson(response, status, MAPPER, data);
    }

    public static void flushResponseJson(HttpServletResponse response, int status,
                                         ObjectMapper mapper, Object data) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(status);
            PrintWriter writer = response.getWriter();
            if (ObjectUtils.isNotEmpty(data)) {
                writer.write(mapper.writeValueAsString(data));
            } else {
                writer.write("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse,
                                                       HttpHeaders header) {
        return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
