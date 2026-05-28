/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.message.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.web.util.HtmlUtils;

import cn.frank.ulp.common.exception.MailTemplateException;

/**
 * 邮件工具类
 *
 * @author Frank Zhang
 */
public class MailUtils {
    /**
     * 获取内容文件
     *
     * @param resourceLocation {@link File}
     * @return {@link File}
     */
    public static String readEmailContent(@NonNull String resourceLocation) {
        try {
            ClassPathResource resource = new ClassPathResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            content = StringUtils.remove(content,
                StringUtils.substringBetween(content, "<!--", "-->"));
            content = StringUtils.remove(StringUtils.remove(content, "<!--"), "-->");
            content = HtmlUtils.htmlEscape(content);
            return content;
        } catch (IOException e) {
            throw new MailTemplateException("获取文件模板失败", e);
        }
    }

}
