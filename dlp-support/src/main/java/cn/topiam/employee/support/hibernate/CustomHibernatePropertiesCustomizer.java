/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.hibernate;

import java.util.Map;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

public class CustomHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {

    private static final String FORMAT_SQL              = "hibernate.format_sql";

    private static final String SHOW_SQL                = "hibernate.show_sql";

    private static final String USE_SQL_COMMENTS        = "hibernate.use_sql_comments";

    private static final String QUERY_SUBSTITUTIONS     = "hibernate.query.substitutions";

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
