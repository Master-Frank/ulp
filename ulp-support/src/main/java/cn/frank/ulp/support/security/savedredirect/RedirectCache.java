/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.security.savedredirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RedirectCache {

    String ULP_SECURITY_SAVED_REDIRECT = "ULP_SECURITY_SAVED_REDIRECT";

    SavedRedirect getRedirect(HttpServletRequest request, HttpServletResponse response);

    void removeRedirect(HttpServletRequest request, HttpServletResponse response);

    void saveRedirect(HttpServletRequest request, HttpServletResponse response,
                      RedirectType redirectType);

    enum RedirectType {
                       PARAMETER, REQUEST
    }
}
