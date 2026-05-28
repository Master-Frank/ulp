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
package cn.frank.ulp.protocol.form.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import lombok.Getter;

/**
 *
 * @author Frank Zhang
 */
@Getter
public class FormAuthenticationException extends AuthenticationException {

    private final FormError error;

    public FormAuthenticationException(FormError error) {
        super(error.getDescription());
        this.error = error;
    }

    public FormAuthenticationException(FormError error, Throwable cause) {
        this(error, error.getDescription(), cause);
    }

    public FormAuthenticationException(FormError error, String message, Throwable cause) {
        super(message, cause);
        Assert.notNull(error, "error cannot be null");
        this.error = error;
    }
}
