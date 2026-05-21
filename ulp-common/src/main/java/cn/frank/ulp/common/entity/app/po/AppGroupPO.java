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
package cn.frank.ulp.common.entity.app.po;

import java.time.LocalDateTime;

import cn.frank.ulp.common.entity.app.AppGroupEntity;
import cn.frank.ulp.common.enums.app.AppGroupType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/13 23:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppGroupPO extends AppGroupEntity {

    /**
     * 应用数量
     */
    private String appCount;

    public AppGroupPO(String id, String name, String code, AppGroupType type,
                      LocalDateTime createTime, String remark, Long appCount) {
        super.setId(id);
        super.setName(name);
        super.setCode(code);
        super.setType(type);
        super.setCreateTime(createTime);
        super.setRemark(remark);
        this.appCount = String.valueOf(appCount);
    }

}
