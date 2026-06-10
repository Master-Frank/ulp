/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.web.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 统一 favicon 服务（GET /favicon.ico）
 *
 * 放在 ulp-support 而非各前端 public/ 的原因：
 * ulp-console / ulp-portal / ulp-openapi 三个服务都依赖本模块，
 * 其中 ulp-openapi 没有前端，若 favicon 只随前端打包则该服务无图标。
 * 将图标集中在此处可保证三个服务使用同一品牌图标，改动时只需更新一处。
 *
 * 如需替换 favicon，更新以下两处文件（前端 public/favicon.ico 不会生效）：
 *   ulp-support/src/main/resources/static/favicon.ico
 *   ulp-support/src/main/resources/META-INF/resources/favicon.ico
 */
@Controller
public class FaviconController {

    private static final String FAVICON_PATH       = "static/favicon.ico";

    private static final String IMAGE_X_ICON_VALUE = "image/x-icon";

    @GetMapping(value = { "/favicon.ico" }, produces = { IMAGE_X_ICON_VALUE })
    @ResponseBody
    public ResponseEntity<Resource> favicon(HttpServletRequest request) {
        ClassPathResource favicon = new ClassPathResource(FAVICON_PATH);
        return ResponseEntity.ok().body(favicon);
    }
}
