/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.event;

import java.io.Serial;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import cn.frank.ulp.audit.entity.*;

import lombok.Getter;

/**
 * 审计事件
 *
 * @author Frank Zhang
 */
@Getter
public class AuditEvent extends ApplicationEvent {
    @Serial
    private static final long  serialVersionUID = 3425943796938543659L;

    private final String       requestId;
    private final String       sessionId;
    private final Actor        actor;
    private final Event        event;
    private final List<Target> targets;
    private final UserAgent    userAgent;
    private final GeoLocation  geoLocation;

    public AuditEvent(String requestId, String sessionId, Actor actor, Event event,
                      UserAgent userAgent, GeoLocation geoLocation, List<Target> targets) {
        // ApplicationEvent forbids a null source. requestId comes from MDC via TraceUtils,
        // which can be empty when an event fires outside the servlet thread (e.g., async
        // listeners, scheduled jobs) or inside a MockMvc filter chain that did not run the
        // TraceFilter. Falling back to a sentinel keeps publishing safe and audit-correlatable.
        super(requestId != null ? requestId : "no-trace");
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.actor = actor;
        this.event = event;
        this.targets = targets;
        this.userAgent = userAgent;
        this.geoLocation = geoLocation;
    }
}
