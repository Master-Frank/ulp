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
package cn.frank.ulp.portal.controller;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.core.security.session.ClusterSessionRegistryImpl;
import cn.frank.ulp.core.security.session.Session;
import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.util.SecurityUtils;
import cn.frank.ulp.support.util.HttpResponseUtils;
import cn.frank.ulp.support.web.useragent.UserAgent;

import lombok.Data;
import lombok.experimental.Accessors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.common.constant.SessionConstants.SESSION_PATH;
import static cn.frank.ulp.support.constant.UlpConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 会话管理
 *
 * @author Frank Zhang
 */
@Tag(name = "会话管理")
@RestController
@RequestMapping(value = SESSION_PATH)
public class SessionManageEndpoint {

    private final SessionRegistry     sessionRegistry;
    private final OnlineUserConverter onlineUserConverter;

    public SessionManageEndpoint(SessionRegistry sessionRegistry,
                                 OnlineUserConverter onlineUserConverter) {
        this.sessionRegistry = sessionRegistry;
        this.onlineUserConverter = onlineUserConverter;
    }

    /**
     * list
     *
     * @param req  {@link HttpServletRequest}
     * @return {@link ApiRestResult}
     */
    @Operation(summary = "在线会话")
    @GetMapping("/list")
    public ApiRestResult<List<OnlineSession>> list(HttpServletRequest req) {
        List<OnlineSession> list = new ArrayList<>();
        if (sessionRegistry instanceof ClusterSessionRegistryImpl) {
            List<SessionInformation> sessions = sessionRegistry
                .getAllSessions(SecurityUtils.getCurrentUser(), false);
            //封装数据
            String sessionId = req.getSession(false).getId();
            List<String> sessionIds = sessions.stream().map(SessionInformation::getSessionId)
                .filter(i -> !i.equals(sessionId)).toList();
            List<Session> details = ((ClusterSessionRegistryImpl<?>) sessionRegistry)
                .getSessionList(sessionIds);
            details.forEach(i -> list.add(onlineUserConverter.sessionDetailsToOnlineSession(i)));
        }
        // 封装返回
        return ApiRestResult.<List<OnlineSession>> builder().result(list).build();
    }

    /**
     * remove
     *
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return {@link ApiRestResult}
     */
    @Operation(summary = "下线会话")
    @Audit(type = EventType.DOWN_LINE_SESSION)
    @DeleteMapping("/remove")
    public ApiRestResult<Boolean> remove(HttpServletRequest req, HttpServletResponse resp) {
        String sessionIds = req.getParameter("sessionIds");
        //session id blank
        if (!StringUtils.isNoneBlank(sessionIds)) {
            HttpResponseUtils.flushResponseJson(resp, HttpStatus.OK.value(),
                ApiRestResult.err("会话ID不存在"));
        }
        String[] ids = sessionIds.split(",");
        Arrays.stream(ids).forEach((i) -> {
            //如果sessionId等于当前操作用户sessionId不操作
            if (!req.getSession(false).getId().equals(i)) {
                AuditContext
                    .setTarget(Target.builder().id(i).name("").type(TargetType.SESSION).build());
                sessionRegistry.getSessionInformation(i).expireNow();
            }
        });
        //返回
        return ApiRestResult.ok();
    }

}

/**
 * 在线用户
 *
 * @author Frank Zhang
 */
@Data
@Accessors(chain = true)
class OnlineSession implements Serializable {

    @Serial
    private static final long serialVersionUID = 8227098865368453321L;
    /**
     * 用户ID
     */
    private String            id;
    /**
     * 用户名
     */
    private String            username;

    /**
     * 活动地点
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private GeoLocation       geoLocation;

    /**
     * 用户代理
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private UserAgent         userAgent;

    /**
     * 认证类型
     */
    private String            authType;

    /**
     * 登录时间
     */
    @JSONField(format = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime     loginTime;

    /**
     * 最后请求时间
     */
    @JSONField(format = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime     lastRequest;

    /**
     * session ID
     */
    private String            sessionId;

}

@Mapper(componentModel = "spring")
interface OnlineUserConverter {
    /**
     * 系统用户转在线会话
     *
     * @param session {@link Session}
     * @return {@link OnlineSession}
     */
    default OnlineSession sessionDetailsToOnlineSession(Session session) {
        if (session == null) {
            return null;
        }

        OnlineSession onlineSession = new OnlineSession();
        //ID
        onlineSession.setId(session.getId());
        //用户名
        onlineSession.setUsername(session.getUsername());
        //session id
        onlineSession.setSessionId(session.getSessionId());
        //地理位置
        onlineSession.setGeoLocation(session.getGeoLocation());
        //用户代理
        onlineSession.setUserAgent(session.getUserAgent());
        //认证类型
        onlineSession.setAuthType(session.getAuthenticationProvider());
        //登录时间
        onlineSession.setLoginTime(session.getAuthenticationTime());
        //最后请求时间
        onlineSession.setLastRequest(session.getLastRequestTime());
        return onlineSession;
    }
}
