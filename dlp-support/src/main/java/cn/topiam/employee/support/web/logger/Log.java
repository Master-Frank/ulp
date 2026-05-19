package cn.topiam.employee.support.web.logger;

import java.util.Map;

import cn.topiam.employee.support.web.useragent.UserAgent;

import lombok.Data;

/**
 * Web 请求日志条目.
 */
@Data
public class Log {

    private String              requestUrl;

    private String              ip;

    private String              httpType;

    private String              method;

    private Map<String, String> headers;

    private String              body;

    private Object              parameter;

    private Boolean             success;

    private Object              result;

    private UserAgent           userAgent;

    private Long                timeSpan;
}
