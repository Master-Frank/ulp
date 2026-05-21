/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.hibernate;

import java.util.Map;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

public class CustomHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {

    private static final String FORMAT_SQL               = "hibernate.format_sql";

    private static final String SHOW_SQL                 = "hibernate.show_sql";

    private static final String USE_SQL_COMMENTS         = "hibernate.use_sql_comments";

    private static final String QUERY_SUBSTITUTIONS      = "hibernate.query.substitutions";

    private static final String PHYSICAL_NAMING_STRATEGY = "hibernate.physical_naming_strategy";

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        if (!hibernateProperties.containsKey(FORMAT_SQL)) {
            hibernateProperties.put(FORMAT_SQL, Boolean.FALSE);
        }
        if (!hibernateProperties.containsKey(SHOW_SQL)) {
            hibernateProperties.put(SHOW_SQL, Boolean.FALSE);
        }
        if (!hibernateProperties.containsKey(USE_SQL_COMMENTS)) {
            hibernateProperties.put(USE_SQL_COMMENTS, Boolean.FALSE);
        }
        if (!hibernateProperties.containsKey(QUERY_SUBSTITUTIONS)) {
            hibernateProperties.put(QUERY_SUBSTITUTIONS, "true 1, false 0");
        }
        if (!hibernateProperties.containsKey(PHYSICAL_NAMING_STRATEGY)) {
            hibernateProperties.put(PHYSICAL_NAMING_STRATEGY,
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        }
    }
}
