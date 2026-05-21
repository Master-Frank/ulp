/*
 * ulp-authentication-alipay - United Login Platform
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
package cn.frank.ulp.authentication.alipay.client;

import com.aliyun.tea.NameInMap;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.Validation;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Frank Zhang
 */
@Getter
@Setter
public class AlipaySystemUserInfoShareResponse extends TeaModel {
    @NameInMap("http_body")
    @Validation(required = true)
    public String httpBody;

    @NameInMap("code")
    @Validation(required = true)
    public String code;

    @NameInMap("msg")
    @Validation(required = true)
    public String msg;

    @NameInMap("sub_code")
    @Validation(required = true)
    public String subCode;

    @NameInMap("sub_msg")
    @Validation(required = true)
    public String subMsg;

    @NameInMap("user_id")
    @Validation(required = true)
    public String userId;

    @NameInMap("avatar")
    @Validation(required = true)
    public String avatar;

    @NameInMap("city")
    @Validation(required = true)
    public Long   city;

    @NameInMap("nick_name")
    @Validation(required = true)
    public String nickName;

    @NameInMap("province")
    @Validation(required = true)
    public Long   province;

    @NameInMap("gender")
    @Validation(required = true)
    public Long   gender;

}
