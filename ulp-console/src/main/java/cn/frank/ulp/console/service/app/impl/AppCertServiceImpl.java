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

import java.util.List;

import org.springframework.stereotype.Service;

import cn.frank.ulp.common.entity.app.AppCertEntity;
import cn.frank.ulp.common.repository.app.AppCertRepository;
import cn.frank.ulp.console.converter.app.AppCertConverter;
import cn.frank.ulp.console.pojo.query.app.AppCertQuery;
import cn.frank.ulp.console.pojo.result.app.AppCertListResult;
import cn.frank.ulp.console.service.app.AppCertService;

import lombok.AllArgsConstructor;

/**
 * 应用证书
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:46
 */
@Service
@AllArgsConstructor
public class AppCertServiceImpl implements AppCertService {

    /**
     * 获取应用证书列表
     *
     * @param query {@link AppCertQuery}
     * @return {@link List}
     */
    @Override
    public List<AppCertListResult> getAppCertListResult(AppCertQuery query) {
        List<AppCertEntity> list = appCertRepository
            .findAll(appCertConverter.queryAppCertListParamConvertToExample(query));
        return appCertConverter.entityConvertToAppCertListResult(list);
    }

    private final AppCertRepository appCertRepository;

    private final AppCertConverter  appCertConverter;
}
