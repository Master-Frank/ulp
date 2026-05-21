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
 * @author Frank Zhang
 */
@Configuration
public class UlpLiquibaseConfiguration implements Serializable {

    /**
     * ULP Liquibase
     *
     * @param dataSource {@link DataSource}
     * @return {@link SpringLiquibase}
     */
    @Bean
    public SpringLiquibase ulpLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath*:db/ulp-changelog-master.xml");
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(true);
        liquibase.setResourceLoader(new DefaultResourceLoader());
        liquibase.setDatabaseChangeLogTable("ulp_changelog_table");
        liquibase.setDatabaseChangeLogLockTable("ulp_changelog_lock_table");
        return liquibase;
    }
}
