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
package cn.frank.ulp.common.enums;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import static org.springframework.http.MediaType.*;

/**
 * View Content Type
 *
 * @author Frank Zhang
 */
public enum ViewContentType {
                             /**
                              * DEFAULT
                              */
                             DEFAULT("default", APPLICATION_OCTET_STREAM_VALUE),
                             /**
                              * JPG
                              */
                             JPG("jpg", IMAGE_JPEG_VALUE),
                             /**
                              * GIF
                              */
                             GIF("gif", IMAGE_GIF_VALUE),
                             /**
                              * JPG
                              */
                             JFIF("jfif", IMAGE_JPEG_VALUE),
                             /**
                              * PNG
                              */
                             PNG("png", IMAGE_PNG_VALUE),
                             /**
                              * ICO
                              */
                             ICO("ico", "image/vnd.microsoft.icon"),
                             /**
                              * JPEG
                              */
                             JPEG("jpeg", IMAGE_JPEG_VALUE),
                             /**
                              * SVG
                              */
                             SVG("svg", "image/svg+xml"),
                             /**
                              * JPE
                              */
                             JPE("jpe", IMAGE_JPEG_VALUE);

    private String suffix;
    private String type;

    public static String getContentType(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return DEFAULT.getType();
        }
        String suffix = FilenameUtils.getExtension(fileName);
        for (ViewContentType value : ViewContentType.values()) {
            if (suffix.equalsIgnoreCase(value.getSuffix())) {
                return value.getType();
            }
        }
        return DEFAULT.getType();
    }

    public static String getSuffix(String type) {
        for (ViewContentType value : ViewContentType.values()) {
            if (type.equalsIgnoreCase(value.getType())) {
                return value.getSuffix();
            }
        }
        return DEFAULT.getSuffix();
    }

    ViewContentType(String prefix, String type) {
        this.suffix = prefix;
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getType() {
        return type;
    }
}
