/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.configuration;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

import liquibase.integration.spring.SpringLiquibase;

/**
 * liquibase配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/28 21:52
 */
@Configuration
public class EiamLiquibaseConfiguration implements Serializable {

    /**
     * TopIAM Liquibase
     *
     * @param dataSource {@link DataSource}
     * @return {@link SpringLiquibase}
     */
    @Bean
    public SpringLiquibase topIamLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath*:db/eiam-changelog-master.xml");
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(true);
        liquibase.setResourceLoader(new DefaultResourceLoader());
        liquibase.setDatabaseChangeLogTable("eiam_changelog_table");
        liquibase.setDatabaseChangeLogLockTable("eiam_changelog_lock_table");
        return liquibase;
    }
}
