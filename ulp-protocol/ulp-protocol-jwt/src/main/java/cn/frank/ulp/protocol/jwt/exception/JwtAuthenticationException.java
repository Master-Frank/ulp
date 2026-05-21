/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import lombok.Getter;

/**
 *
 * @author Frank Zhang
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final JwtError error;

    public JwtAuthenticationException(JwtError error) {
        super(error.getDescription());
        this.error = error;
    }

    public JwtAuthenticationException(JwtError error, Throwable cause) {
        this(error, error.getDescription(), cause);
    }

    public JwtAuthenticationException(JwtError error, String message, Throwable cause) {
        super(message, cause);
        Assert.notNull(error, "error cannot be null");
        this.error = error;
    }
}
