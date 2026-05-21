/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.util.HttpResponseUtils;

import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.support.exception.enums.ExceptionStatus.EX000203;

/**
 * session 过期
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/7/25 21:08
 */
public class ConsoleSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) {
        HttpServletResponse response = event.getResponse();
        ApiRestResult<String> result = ApiRestResult.<String> builder().status(EX000203.getCode())
            .message(EX000203.getMessage()).build();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        HttpResponseUtils.flushResponse(response, JSONObject.toJSONString(result));
    }
}
