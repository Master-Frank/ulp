/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.initializer;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.support.config.AbstractSystemInitializer;
import cn.frank.ulp.support.config.InitializationException;
import cn.frank.ulp.support.util.AesUtils;
import static cn.frank.ulp.common.constant.SettingConstants.AES_SECRET;

/**
 * EncryptSecretInitializer
 *
 * @author Frank Zhang
 */
@Component
public class EncryptSecretInitializer extends AbstractSystemInitializer {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        SettingEntity optional = settingRepository.findByName(AES_SECRET);
        if (Objects.isNull(optional)) {
            SettingEntity setting = new SettingEntity();
            setting.setName(AES_SECRET);
            setting.setValue(AesUtils.generateKey());
            setting.setDesc("Project aes secret");
            setting.setRemark(
                "This aes secret is automatically created during system initialization.");
            settingRepository.save(setting);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    /**
     * SettingRepository
     */
    private final SettingRepository settingRepository;

    /**
     *
     * @param settingRepository {@link SettingRepository}
     */
    public EncryptSecretInitializer(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }
}
