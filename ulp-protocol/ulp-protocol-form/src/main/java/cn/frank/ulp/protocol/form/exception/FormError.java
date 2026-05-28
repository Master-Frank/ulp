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

import java.io.Serial;
import java.io.Serializable;

import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

/**
 * @author Frank Zhang
 */
public class FormError implements Serializable {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String      errorCode;

    private final String      description;

    private final String      uri;

    /**
     * Constructs an {@code FormError} using the provided parameters.
     * @param errorCode the error code
     */
    public FormError(String errorCode) {
        this(errorCode, null, null);
    }

    /**
     * Constructs an {@code FormError} using the provided parameters.
     * @param errorCode the error code
     * @param description the error description
     * @param uri the error uri
     */
    public FormError(String errorCode, String description, String uri) {
        Assert.hasText(errorCode, "errorCode cannot be empty");
        this.errorCode = errorCode;
        this.description = description;
        this.uri = uri;
    }

    /**
     * Returns the error code.
     * @return the error code
     */
    public final String getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the error description.
     * @return the error description
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * Returns the error uri.
     * @return the error uri
     */
    public final String getUri() {
        return this.uri;
    }

    @Override
    public String toString() {
        return "[" + this.getErrorCode() + "] "
               + ((this.getDescription() != null) ? this.getDescription() : "");
    }

}
