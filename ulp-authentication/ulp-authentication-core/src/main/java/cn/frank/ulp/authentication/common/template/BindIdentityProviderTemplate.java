/*
 * ulp-authentication-core - United Login Platform
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
package cn.frank.ulp.authentication.common.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.support.exception.TemplateNotExistException;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.extern.slf4j.Slf4j;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Frank Zhang
 */
@Slf4j
public class BindIdentityProviderTemplate {

    public BindIdentityProviderTemplate() {
        configFreemarkerTemplate();
    }

    /**
     * 返回绑定模版
     *
     * @param response       {@link HttpServletResponse}
     * @param result       {@link ApiRestResult <Void>}
     */
    public void sendAuthorizationResponse(HttpServletResponse response,
                                          ApiRestResult<Void> result) throws IOException {
        //@formatter:off
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(ContentType.TEXT_HTML.getMimeType());
            Template template = freemarkerTemplateConfiguration.getTemplate("bind_redirect.ftlh");
            Map<String, Object> data = new HashMap<>(16);
            data.put("nonce", System.currentTimeMillis());
            ObjectMapper objectMapper = new ObjectMapper();
            data.put("result", objectMapper.writeValueAsString(result));
            template.process(data, response.getWriter());
        } catch (Exception e) {
            log.error("返回绑定提供商Template异常", e);
            //Html
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        //@formatter:on
    }

    private void configFreemarkerTemplate() {
        try {
            //模板存放路径
            freemarkerTemplateConfiguration
                .setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/template/"));
            //编码
            freemarkerTemplateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.name());
            //国际化
            freemarkerTemplateConfiguration.setLocale(new Locale("zh_CN"));
        } catch (Exception exception) {
            throw new TemplateNotExistException(exception);
        }
    }

    /**
     * freemarker 配置实例化
     */
    private final Configuration freemarkerTemplateConfiguration = new Configuration(
        Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
}
