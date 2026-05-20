/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.savedredirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RedirectCache {

    String TOPIAM_SECURITY_SAVED_REDIRECT = "TOPIAM_SECURITY_SAVED_REDIRECT";

    SavedRedirect getRedirect(HttpServletRequest request, HttpServletResponse response);

    void removeRedirect(HttpServletRequest request, HttpServletResponse response);

    void saveRedirect(HttpServletRequest request, HttpServletResponse response, RedirectType redirectType);

    enum RedirectType {
        PARAMETER,
        REQUEST
    }
}
