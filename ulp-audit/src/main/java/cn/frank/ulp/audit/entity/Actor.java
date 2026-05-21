/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.entity;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.support.security.userdetails.UserType;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Actor
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:30
 */
@Data
@Builder
public class Actor implements Serializable {

    public static final String                                       ACTOR_ID         = "actor.id.keyword";
    public static final String                                       ACTOR_TYPE       = "actor.type.keyword";

    public static final String                                       ACTOR_AUTH_TYPE  = "actor.auth_type.keyword";

    @Serial
    private static final long                                        serialVersionUID = -1144169992714000310L;
    /**
     * 行动者ID
     */
    @NonNull
    private String                                                   id;

    /**
     * 行动者类型
     */
    @NonNull
    private UserType type;

    /**
     * 身份验证类型
     */
    private String                                                   authType;
}
