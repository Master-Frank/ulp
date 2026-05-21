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
package cn.frank.ulp.console.service.app.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.frank.ulp.application.ApplicationService;
import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.common.enums.app.AppType;
import cn.frank.ulp.console.pojo.result.app.AppTemplateResult;
import cn.frank.ulp.console.service.app.AppTemplateService;

import lombok.AllArgsConstructor;

/**
 * ApplicationTemplateServiceImpl
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/29 22:23
 */
@Service
@AllArgsConstructor
public class AppTemplateServiceImpl implements AppTemplateService {

    /**
     * List
     *
     * @param type {@link AppType}
     * @return {@link List}
     */
    @Override
    public List<AppTemplateResult> getAppTemplateList(AppType type, String name) {
        List<AppTemplateResult> results = new ArrayList<>();
        List<ApplicationService> list = applicationServiceLoader.getApplicationServiceList();
        if (StringUtils.isNotBlank(name)) {
            list = list.stream()
                .filter(applicationService -> applicationService.getName().contains(name)).toList();
        }
        for (ApplicationService protocol : list) {
            if (protocol.getType().equals(type)) {
                AppTemplateResult result = new AppTemplateResult();
                result.setProtocol(protocol.getProtocol());
                result.setCode(protocol.getCode());
                result.setDesc(protocol.getDescription());
                result.setIcon(protocol.getBase64Icon());
                result.setName(protocol.getName());
                result.setType(protocol.getType());
                results.add(result);
            }
        }
        return results;
    }

    /**
     * List
     *
     * @param code {@link Map}
     * @return {@link List}
     */
    @Override
    public Object getAppTemplateFormSchema(String code) {
        ApplicationService applicationService = applicationServiceLoader
            .getApplicationService(code);
        if (!Objects.isNull(applicationService)) {
            return applicationService.getFormSchema();
        }
        return new ArrayList<>();
    }

    private final ApplicationServiceLoader applicationServiceLoader;

}
