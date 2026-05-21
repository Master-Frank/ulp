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
package cn.frank.ulp.support.security.util;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

public final class SecurityUtils {

    public static final String                       ANONYMOUS_USER                = "anonymousUser";

    private static final AuthenticationTrustResolver AUTHENTICATION_TRUST_RESOLVER = new AuthenticationTrustResolverImpl();

    private SecurityUtils() {
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static boolean isAuthenticated() {
        Authentication authentication = getSecurityContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return false;
        }
        if (AUTHENTICATION_TRUST_RESOLVER.isAnonymous(authentication)) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public static boolean isPrincipalAuthenticated(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        if (AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public static boolean isCurrentUserInRole(String role) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role)))
            .orElse(Boolean.FALSE);
    }

    public static String getCurrentUserName() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(authentication -> {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    return ((UserDetails) principal).getUsername();
                }
                if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                    return ((org.springframework.security.core.userdetails.UserDetails) principal)
                        .getUsername();
                }
                if (principal instanceof String) {
                    return (String) principal;
                }
                return null;
            }).orElse(ANONYMOUS_USER);
    }

    public static String getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(authentication -> {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    return ((UserDetails) principal).getId();
                }
                if (principal instanceof String) {
                    return (String) principal;
                }
                return null;
            }).orElse(ANONYMOUS_USER);
    }

    public static UserDetails getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(authentication -> {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    return (UserDetails) principal;
                }
                return null;
            }).orElse(null);
    }

    public static UserType getCurrentUserType() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(authentication -> {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    return ((UserDetails) principal).getUserType();
                }
                return null;
            }).orElse(null);
    }

    public static String getPrincipal(AbstractAuthenticationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal)
                .getUsername();
        }
        if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }
}
