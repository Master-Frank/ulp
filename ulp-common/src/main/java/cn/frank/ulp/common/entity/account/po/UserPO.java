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

import cn.frank.ulp.common.entity.account.UserEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 PO
 *
 * @author TopIAM
 * Created by support@topiam.cn on  2022/2/10 22:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPO extends UserEntity {

    @Serial
    private static final long serialVersionUID = 2330202241971348786L;

    /**
     * 组织机构显示目录
     */
    private String            orgDisplayPath;

    public UserPO(UserEntity user, String orgDisplayPath) {
        super.setId(user.getId());
        super.setUsername(user.getUsername());
        super.setPassword(user.getPassword());
        super.setEmail(user.getEmail());
        super.setPhone(user.getPhone());
        super.setPhoneAreaCode(user.getPhoneAreaCode());
        super.setFullName(user.getFullName());
        super.setNickName(user.getNickName());
        super.setAvatar(user.getAvatar());
        super.setStatus(user.getStatus());
        super.setDataOrigin(user.getDataOrigin());
        super.setEmailVerified(user.getEmailVerified());
        super.setPhoneVerified(user.getPhoneVerified());
        super.setAuthTotal(user.getAuthTotal());
        super.setLastAuthIp(user.getLastAuthIp());
        super.setLastAuthTime(user.getLastAuthTime());
        super.setExpand(user.getExpand());
        super.setExternalId(user.getExternalId());
        super.setExpireDate(user.getExpireDate());
        super.setCreateBy(user.getCreateBy());
        super.setCreateTime(user.getCreateTime());
        super.setUpdateBy(user.getUpdateBy());
        super.setUpdateTime(user.getUpdateTime());
        super.setRemark(user.getRemark());
        setOrgDisplayPath(orgDisplayPath);
    }
}
