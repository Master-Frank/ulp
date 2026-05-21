/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import cn.frank.ulp.core.context.ContextService;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.util.HttpResponseUtils;
import cn.frank.ulp.support.util.UrlUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.support.context.ServletContextService.isHtmlRequest;
import static cn.frank.ulp.support.result.ApiRestResult.SUCCESS;

/**
 * 注销成功
 *
 * @author Frank Zhang
 */
public class PortalLogoutSuccessHandler implements
                                        org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        //@formatter:off
        boolean isHtmlRequest = isHtmlRequest(request);
        if (response.isCommitted()) {
            return;
        }
        if (!isHtmlRequest) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                    ApiRestResult.builder().status(SUCCESS).build());
            return;
        }
        response.sendRedirect(UrlUtils.format(ContextService.getPortalLoginUrl()));
        //@formatter:on
    }
}
