/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.context;

import java.io.Serializable;

import org.springframework.util.Assert;

import lombok.Data;

/**
 * Public base URL builder
 *
 * @author Frank Zhang
 * Created by support on 2020/8/18 21:25
 */
@Data
public class PublicBaseUrlBuilder implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8043605311513960464L;

    /**
     * Protocol scheme (http/https)
     */
    private String            scheme;

    /**
     * Server name
     */
    private String            serverName;

    /**
     * Port number
     */
    private Integer           port;

    /**
     * Context path
     */
    private String            contextPath;

    /**
     * Build URL
     *
     * @return {@link String}
     */
    public String build() {
        PublicBaseUrlBuilder builder = this;
        Assert.notNull(builder.scheme, "scheme不能为空");
        Assert.notNull(builder.serverName, "serverName不能为空");
        StringBuilder sb = new StringBuilder();
        sb.append(builder.scheme).append("://").append(builder.serverName);
        if (builder.port != null && !"http".equals(builder.scheme)
            && !"https".equals(builder.scheme)) {
            sb.append(":").append(builder.port);
        }
        if (builder.contextPath != null) {
            sb.append(builder.contextPath);
        }
        return sb.toString();
    }

    public PublicBaseUrlBuilder setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public PublicBaseUrlBuilder setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public PublicBaseUrlBuilder setPort(Integer port) {
        this.port = port;
        return this;
    }

    public PublicBaseUrlBuilder setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }
}
