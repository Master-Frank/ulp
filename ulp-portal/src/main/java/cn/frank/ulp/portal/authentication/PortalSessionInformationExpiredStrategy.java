/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.portal.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;

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
public class PortalSessionInformationExpiredStrategy implements
                                                     org.springframework.security.web.session.SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) {
        HttpServletResponse response = event.getResponse();
        ApiRestResult<String> result = ApiRestResult.<String> builder().status(EX000203.getCode())
            .message(EX000203.getMessage()).build();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        HttpResponseUtils.flushResponse(response, JSONObject.toJSONString(result));
    }
}
