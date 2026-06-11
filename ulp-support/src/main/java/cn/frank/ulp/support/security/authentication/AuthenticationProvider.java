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
package cn.frank.ulp.support.security.authentication;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 认证提供者类
 * 表示系统支持的认证方式
 */
public class AuthenticationProvider implements Serializable {
    /**
    * 认证提供者名称
    */
    private final String                       name;

    private static final long                  serialVersionUID = 5556479135569165620L;

    /**
    * 用户名密码认证提供者实例
    */
    public static final AuthenticationProvider USERNAME_PASSWORD;

    /**
    * 认证提供者类型
    */
    private final String                       type;

    static {
        USERNAME_PASSWORD = new AuthenticationProvider("用户名密码认证", "USERNAME_PASSWORD");
    }

    /**
    * 判断对象是否相等
    *
    * @param object 比较对象
    * @return 是否相等
    */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            AuthenticationProvider that = (AuthenticationProvider) object;
            return (new EqualsBuilder()).append(this.type, that.type).append(this.name, that.name)
                .isEquals();
        } else {
            return false;
        }
    }

    /**
    * 获取认证提供者名称
    *
    * @return 认证提供者名称
    */
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    /**
    * 获取认证提供者类型
    *
    * @return 认证提供者类型
    */
    @JsonProperty("type")
    public String getType() {
        return this.type;
    }

    /**
    * 生成哈希码
    *
    * @return 哈希码
    */
    @Override
    public int hashCode() {
        return (new HashCodeBuilder(17, 37)).append(this.type).append(this.name).toHashCode();
    }

    /**
    * 构造函数
    *
    * @param type 认证提供者类型
    * @param name 认证提供者名称
    */
    @JsonCreator
    public AuthenticationProvider(@JsonProperty("type") String type,
                                  @JsonProperty("name") String name) {
        this.type = type;
        this.name = name;
    }
}
