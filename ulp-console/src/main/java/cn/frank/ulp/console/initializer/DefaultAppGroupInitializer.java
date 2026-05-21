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

import java.util.Arrays;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.app.AppGroupEntity;
import cn.frank.ulp.common.enums.app.AppDefaultGroup;
import cn.frank.ulp.common.enums.app.AppGroupType;
import cn.frank.ulp.common.repository.app.AppGroupRepository;
import cn.frank.ulp.support.config.AbstractSystemInitializer;
import cn.frank.ulp.support.config.InitializationException;

/**
 * DefaultAppGroupInitialize
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/11 21:44
 */
@Order(2)
@Component
public class DefaultAppGroupInitializer extends AbstractSystemInitializer {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        //@formatter:off
        Arrays.stream(AppDefaultGroup.values()).toList().forEach(i -> {
            Optional<AppGroupEntity> optional = appGroupRepository.findByCode(i.getCode());
            if (optional.isEmpty()) {
                AppGroupEntity appGroup = new AppGroupEntity();
                appGroup.setCode(i.getCode());
                appGroup.setName(i.getDesc());
                appGroup.setType(AppGroupType.DEFAULT);
                appGroup.setRemark(
                        "This app group is automatically created during system initialization.");
                appGroupRepository.save(appGroup);
            }
        });
    }
    @Override
    public int getOrder() {
        return 4;
    }

    private final AppGroupRepository        appGroupRepository;


    public DefaultAppGroupInitializer(AppGroupRepository appGroupRepository) {
        this.appGroupRepository = appGroupRepository;
    }
}
