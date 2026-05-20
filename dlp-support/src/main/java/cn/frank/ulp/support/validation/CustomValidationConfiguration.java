/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.validation;

import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

public class CustomValidationConfiguration {

    @Bean
    public ValidationConfigurationCustomizer validationConfigurationCustomizer() {
        return (configuration) -> configuration
            .addProperty("hibernate.validator.fail_fast", "true");
    }
}
