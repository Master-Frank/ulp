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
package cn.frank.ulp.identitysource.feishu.domain.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.frank.ulp.identitysource.feishu.domain.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 列表反参
 *
 * @author Frank Zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetListResponse<T extends Serializable> extends BaseResponse {
    private ListData<T> data;

    /**
     * 列表信息
     * @param <T>
     */
    @Data
    public static class ListData<T extends Serializable> implements Serializable {
        /**
         * 是否还有更多项
         */
        @JsonProperty("has_more")
        private boolean hasMore;
        /**
         * 分页标记，当 has_more 为 true 时，会同时返回新的 page_token，否则不返回 page_token
         */
        @JsonProperty("page_token")
        private String  pageToken;
        /**
         * 部门列表
         */
        private List<T> items;
    }
}
