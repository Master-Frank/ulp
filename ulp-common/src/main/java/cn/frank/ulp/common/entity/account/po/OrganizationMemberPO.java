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
package cn.frank.ulp.common.entity.account.po;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 组织 PO
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/30 21:27
 */
@Getter
@Setter
public class OrganizationMemberPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -150631305460653395L;

    /**
     * 主键ID
     */
    private String            id;

    /**
     * 用户ID
     */
    private String            userId;

    /**
     * 组织ID
     */
    private String            orgId;

    /**
     * 组织名称
     */
    private String            orgName;

    /**
     * 显示路径
     */
    private String            displayPath;

    public OrganizationMemberPO(String id, String userId, String orgId, String orgName,
                                String displayPath) {
        this.id = String.valueOf(id);
        this.userId = String.valueOf(userId);
        this.orgId = orgId;
        this.orgName = orgName;
        this.displayPath = displayPath;
    }
}
