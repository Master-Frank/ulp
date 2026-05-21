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
package cn.frank.ulp.protocol.form.endpoint;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/19 22:20
 */
public class FormParameterNames {

    /**
     * {@code error} - used in Authorization Response and Access Token Response.
     */
    public static final String ERROR             = "error";

    /**
     * {@code error_description} - used in Authorization Response and Access Token
     * Response.
     */
    public static final String ERROR_DESCRIPTION = "error_description";

    /**
     * {@code error_uri} - used in Authorization Response and Access Token Response.
     */
    public static final String ERROR_URI         = "error_uri";

    private FormParameterNames() {
    }
}
