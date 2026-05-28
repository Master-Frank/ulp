/*
 * ulp-identity-source-dingtalk - United Login Platform
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
package cn.frank.ulp.identitysource.dingtalk.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.enums.BaseEnum;

/**
 * 钉钉事件类型
 * @author Frank Zhang
 */
public enum DingTalkEventType implements BaseEnum {
                                                   /**
                                                    * 测试
                                                    */
                                                   CHECK_URL("check_url", "测试url"),
                                                   /**
                                                    * 用户变更-通讯录用户增加
                                                    */
                                                   USER_ADD_ORG("user_add_org", "通讯录用户增加"),
                                                   /**
                                                    * 通讯录用户更改
                                                    */
                                                   USER_MODIFY_ORG("user_modify_org", "通讯录用户更改"),
                                                   /**
                                                    * 通讯录用户离职
                                                    */
                                                   USER_LEAVE_ORG("user_leave_org", "通讯录用户离职"),
                                                   /**
                                                    * 加入企业后用户激活
                                                    */
                                                   USER_ACTIVE_ORG("user_active_org", "加入企业后用户激活"),
                                                   /**
                                                    * 部门变更-通讯录企业部门创建
                                                    */
                                                   ORG_DEPT_CREATE("org_dept_create", "通讯录企业部门创建"),
                                                   /**
                                                    * 通讯录企业部门修改
                                                    */
                                                   ORG_DEPT_MODIFY("org_dept_modify", "通讯录企业部门修改"),
                                                   /**
                                                    * 通讯录企业部门删除
                                                    */
                                                   ORG_DEPT_REMOVE("org_dept_remove", "通讯录企业部门删除");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * 名称
     */
    private final String name;

    DingTalkEventType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return null;
    }

    public String getName() {
        return name;
    }

    public static DingTalkEventType getType(String code) {
        DingTalkEventType[] values = values();
        for (DingTalkEventType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该类型");
    }

    @Override
    public String toString() {
        return this.code;
    }
}
