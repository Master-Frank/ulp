/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.security.web;

import java.io.IOException;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import cn.frank.ulp.support.security.session.RefreshCurrentSessionPrincipalService;
import cn.frank.ulp.support.security.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionRefreshFilter extends GenericFilterBean {

    private SecurityContextRepository                   securityContextRepository = new HttpSessionSecurityContextRepository();

    private final RefreshCurrentSessionPrincipalService refreshCurrentSessionPrincipalService;

    public SessionRefreshFilter(RefreshCurrentSessionPrincipalService refreshCurrentSessionPrincipalService) {
        this.refreshCurrentSessionPrincipalService = refreshCurrentSessionPrincipalService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
        SecurityContext context = this.securityContextRepository.loadContext(holder);

        Authentication authentication = context.getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String userId = ((UserDetails) principal).getId();
                UserDetails latest = this.refreshCurrentSessionPrincipalService
                    .getPrincipal(userId);
                if (Objects.nonNull(latest)) {
                    UsernamePasswordAuthenticationToken refreshed = UsernamePasswordAuthenticationToken
                        .authenticated(latest, authentication.getCredentials(),
                            latest.getAuthorities());
                    refreshed.setDetails(authentication.getDetails());
                    context.setAuthentication(refreshed);
                    SecurityContextHolder.setContext(context);
                    this.securityContextRepository.saveContext(context, request, response);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }
}
