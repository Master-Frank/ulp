/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.error;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.View;
import org.springframework.web.util.HtmlUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomErrorView implements View {

    private static final MediaType CONTENT_TYPE;

    private static final Log       logger;

    static {
        CONTENT_TYPE = new MediaType("text", "html", StandardCharsets.UTF_8);
        logger = LogFactory.getLog(CustomErrorView.class);
    }

    @Override
    public String getContentType() {
        return "text/html;charset=UTF-8";
    }

    private void logWarningIfNotRendered(@NonNull HttpServletRequest request,
                                         @NonNull HttpServletResponse response,
                                         @NonNull Map<String, ?> map, Exception ex) {
        if (logger.isWarnEnabled()) {
            String message = "Warning: requestId=" + ObjectUtils.identityToString(this)
                             + ", error message=[" + map.get("message") + "]" + ", status code=["
                             + response.getStatus() + "]" + ", exception=[" + ex.getClass().getName()
                             + ": " + ex.getMessage() + "]" + ", request URI=["
                             + request.getRequestURI() + "]" + ", request method=["
                             + request.getMethod() + "]" + ", timestamp=["
                             + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "]";
            logger.warn(message, ex);
        }
    }

    @Override
    public void render(Map<String, ?> map, @NonNull HttpServletRequest request,
                       @NonNull HttpServletResponse response) {
        if (response.isCommitted()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Response already committed. Cannot render error page.");
            }
            return;
        }

        response.setContentType(CONTENT_TYPE.toString());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        Object messageObj = map == null ? null : map.get("message");
        Object statusObj = map == null ? null : map.get("status");
        String escapedMessage = HtmlUtils.htmlEscape(messageObj == null ? "" : messageObj.toString());

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n").append("<html>\n").append("<head>\n")
            .append("    <title>Error</title>\n").append("    <meta charset=\"UTF-8\">\n")
            .append("    <style>\n")
            .append("        body { font-family: Arial, sans-serif; margin: 40px; }\n")
            .append("        .error-container { border: 1px solid #ddd; padding: 20px; border-radius: 5px; }\n")
            .append("        .error-title { color: #d9534f; }\n")
            .append("        .error-message { color: #333; }\n").append("    </style>\n")
            .append("</head>\n").append("<body>\n").append("    <div class=\"error-container\">\n")
            .append("        <h1 class=\"error-title\">系统错误</h1>\n")
            .append("        <p class=\"error-message\">").append(escapedMessage).append("</p>\n")
            .append("        <p>状态码: ").append(statusObj == null ? "" : statusObj).append("</p>\n")
            .append("        <p>时间戳: ").append(new Date()).append("</p>\n")
            .append("    </div>\n").append("</body>\n").append("</html>");

        try {
            response.getWriter().write(html.toString());
        } catch (Exception ex) {
            this.logWarningIfNotRendered(request, response, map, ex);
        }
    }
}
