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
package cn.frank.ulp.support.security.userdetails;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Generated;

/**
 * 用户类型类
 * 用于表示用户的类型信息
 */
public class UserType implements Serializable {
    /**
    * 用户类型名称
    */
    private String               name;

    /**
    * 普通用户类型
    */
    public static final UserType USER  = new UserType("user", "普通用户");

    /**
    * 管理员用户类型
    */
    public static final UserType ADMIN = new UserType("admin", "管理员");

    /**
    * 用户类型
    */
    private String               type;

    /**
    * 默认构造函数
    */
    public UserType() {
    }

    /**
    * 获取用户类型名称
    *
    * @return 用户类型名称
    */
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    /**
    * 设置用户类型
    *
    * @param type 用户类型
    */
    @Generated
    public void setType(String type) {
        this.type = type;
    }

    /**
    * 比较对象是否相等
    *
    * @param other 其他对象
    * @return 是否相等
    */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            UserType userType = (UserType) other;
            return (new EqualsBuilder()).append(this.type, userType.type)
                .append(this.name, userType.name).isEquals();
        } else {
            return false;
        }
    }

    /**
    * 获取用户类型
    *
    * @return 用户类型
    */
    @JsonProperty("type")
    public String getType() {
        return this.type;
    }

    /**
    * 构造函数
    *
    * @param type 用户类型
    * @param name 用户类型名称
    */
    public UserType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
    * 设置用户类型名称
    *
    * @param name 用户类型名称
    */
    @Generated
    public void setName(String name) {
        this.name = name;
    }

    /**
    * 计算哈希值
    *
    * @return 哈希值
    */
    @Override
    public int hashCode() {
        return (new HashCodeBuilder(17, 37)).append(this.type).append(this.name).toHashCode();
    }
}
