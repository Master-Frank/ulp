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
package cn.frank.ulp.audit.event.type;

import java.util.List;

import cn.frank.ulp.audit.event.Type;
import cn.frank.ulp.support.security.userdetails.UserType;
import static cn.frank.ulp.audit.event.ConsoleResource.SESSION_RESOURCE;

/**
 * 系统监控
 *
 * @author Frank Zhang
 */
public class MonitorEventType {

    /**
     * 下线会话
     */
    public static Type DOWN_LINE_SESSION = new Type("ulp:event:monitor:down_line_session", "下线会话",
        SESSION_RESOURCE, List.of(UserType.ADMIN));

}
