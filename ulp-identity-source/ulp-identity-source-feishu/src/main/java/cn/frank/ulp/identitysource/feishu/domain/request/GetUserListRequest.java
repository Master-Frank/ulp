/*
 * ulp-identity-source-feishu - United Login Platform
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
package cn.frank.ulp.identitysource.feishu.domain.request;

import com.alibaba.fastjson2.annotation.JSONField;

import cn.frank.ulp.identitysource.feishu.domain.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import static cn.frank.ulp.identitysource.feishu.FeiShuConstant.PAGE_SIZE;

/**
 * 用户列表入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 22:47
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GetUserListRequest extends BaseRequest {
    /**
     * 填写该字段表示获取该部门下用户，必填。根部门的部门ID为0。
     * 示例值："od-xxxxxxxxxxxxx"
     */
    @JSONField(name = "department_id")
    private String departmentId;
    /**
     * 分页大小
     * 示例值：10
     * 数据校验规则：
     * 最大值：50
     */
    @JSONField(name = "page_size")
    private int    pageSize = PAGE_SIZE;
    /**
     * 分页标记，第一次请求不填，表示从头开始遍历；分页查询结果还有更多项时会同时返回新的 page_token，下次遍历可采用该 page_token 获取查询结果
     * 示例值："AQD9/Rn9eij9Pm39ED40/dk53s4Ebp882DYfFaPFbz00L4CMZJrqGdzNyc8BcZtDbwVUvRmQTvyMYicnGWrde9X56TgdBuS+JKiSIkdexPw="
     */
    @JSONField(name = "page_token")
    private String pageToken;
}
