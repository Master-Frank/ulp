/*
 * eiam-audit - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.audit.event.type;

import java.util.List;

import cn.frank.ulp.audit.event.Type;
import cn.frank.ulp.support.security.userdetails.UserType;
import static cn.frank.ulp.audit.event.ConsoleResource.SESSION_RESOURCE;

/**
 * 系统监控
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/24 22:58
 */
public class MonitorEventType {

    /**
     * 下线会话
     */
    public static Type DOWN_LINE_SESSION = new Type("eiam:event:monitor:down_line_session", "下线会话",
        SESSION_RESOURCE, List.of(UserType.ADMIN));

}
