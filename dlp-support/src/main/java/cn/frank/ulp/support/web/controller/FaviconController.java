/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.web.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FaviconController {

    private static final String FAVICON_PATH = "static/favicon.ico";

    private static final String IMAGE_X_ICON_VALUE = "image/x-icon";

    @GetMapping(value = { "/favicon.ico" }, produces = { IMAGE_X_ICON_VALUE })
    @ResponseBody
    public ResponseEntity<Resource> favicon(HttpServletRequest request) {
        ClassPathResource favicon = new ClassPathResource(FAVICON_PATH);
        return ResponseEntity.ok().body(favicon);
    }
}
