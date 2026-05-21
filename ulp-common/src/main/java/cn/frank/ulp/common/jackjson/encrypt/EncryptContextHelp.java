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
package cn.frank.ulp.common.jackjson.encrypt;

import java.util.Objects;

import org.springframework.util.Assert;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.util.AesUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static cn.frank.ulp.common.constant.SettingConstants.AES_SECRET;

/**
 * EncryptContextHelp
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/22 21:53
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptContextHelp {
    private static final AesUtils AES_UTILS = new AesUtils(getAesSecret());

    public static String encrypt(String content) {
        return AES_UTILS.encrypt(content);
    }

    public static String decrypt(String content) {
        if (Objects.isNull(content)) {
            return null;
        }
        return AES_UTILS.decrypt(content);
    }

    /**
     * 获取AES秘钥
     *
     * @return  {@link String}
     */
    public static String getAesSecret() {
        SettingEntity setting = getSettingRepository().findByName(AES_SECRET);
        Assert.notNull(setting, "aes secret must not be null");
        return setting.getValue();
    }

    private static SettingRepository getSettingRepository() {
        return ApplicationContextService.getBean(SettingRepository.class);
    }
}
