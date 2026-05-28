/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form.endpoint.authentication;

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;

import cn.frank.ulp.application.context.ApplicationContext;
import cn.frank.ulp.application.context.ApplicationContextHolder;
import cn.frank.ulp.application.form.model.FormProtocolConfig;
import cn.frank.ulp.protocol.form.authentication.FormRequestAuthenticationToken;
import cn.frank.ulp.protocol.form.exception.FormAuthenticationException;
import cn.frank.ulp.protocol.form.exception.FormError;

import jakarta.servlet.http.HttpServletRequest;
import static cn.frank.ulp.protocol.form.constant.FormProtocolConstants.FORM_ERROR_URI;
import static cn.frank.ulp.protocol.form.exception.FormErrorCodes.SERVER_ERROR;

/**
 * @author Frank Zhang
 */
public final class FormAuthenticationTokenConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        if (MapUtils.isEmpty(context.getConfig())
            | !context.getConfig().containsKey(FormProtocolConfig.class.getName())) {
            FormError error = new FormError(SERVER_ERROR, null, FORM_ERROR_URI);
            throw new FormAuthenticationException(error);
        }
        FormProtocolConfig config = (FormProtocolConfig) context.getConfig()
            .get(FormProtocolConfig.class.getName());
        return new FormRequestAuthenticationToken(authentication, config);
    }

}
