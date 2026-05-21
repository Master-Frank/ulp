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
package cn.frank.ulp.support.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.net.util.SubnetUtils;

import com.google.common.net.InetAddresses;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

/**
 * IP / 子网工具.
 */
public class IpUtils {

    public static final String IPV4      = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public static final String IPV6      = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(::[fF]{4}(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";

    public static final String IPV4_CIDR = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(?:[12]?[0-9]|3[0-2])$";

    public static final String IPV6_CIDR = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(::[fF]{4}(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))/([0-9]|[1-9][0-9]|1[0-1][0-9]|12[0-8])$";

    public IpUtils() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        try {
            if (request == null) {
                return "Unknown";
            }
            String ip = request.getHeader("X-Forwarded-For");
            if (isUnknown(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (isUnknown(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (isUnknown(ip)) {
                ip = request.getHeader("x-real-ip");
            }
            if (isUnknown(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (isUnknown(ip)) {
                ip = request.getHeader("X-Real-Ip");
            }
            if (isUnknown(ip)) {
                ip = request.getRemoteAddr();
                if (org.apache.commons.lang3.StringUtils.equalsAny(ip, "127.0.0.1",
                    "0:0:0:0:0:0:0:1")) {
                    ip = Objects.toString(InetAddress.getLocalHost().getHostAddress(), "127.0.0.1");
                }
            }
            return org.apache.commons.lang3.StringUtils.substringBefore(ip, ",");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

    public static boolean isInternalIp(String ip) {
        if (ip == null) {
            return false;
        }
        try {
            byte[] addr = InetAddresses.forString(ip).getAddress();
            if (addr.length != 4) {
                return ip.startsWith("127.");
            }
            int b0 = addr[0] & 0xFF;
            int b1 = addr[1] & 0xFF;
            switch (b0) {
                case 10:
                    return true;
                case 172:
                    return b1 >= 16 && b1 <= 31;
                case 192:
                    return b1 == 168;
                case 127:
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            return ip.startsWith("127.");
        }
    }

    public static boolean isInRange(@NotNull String ip, @NotNull String ipRange) {
        if (ipRange.split("/").length == 1) {
            return ip.equals(ipRange);
        }
        return new SubnetUtils(ipRange).getInfo().isInRange(ip);
    }

    public static boolean isInRange(@NotNull String ip, @NotNull List<String> ipScopes) {
        for (String range : ipScopes) {
            if (isInRange(ip, range)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 范围匹配：(ip, startIp, endIp). 把 IP 转为字节数组逐字节比较。
     */
    public static boolean isInRange(String ip, String startIp, String endIp) {
        try {
            byte[] target = InetAddress.getByName(ip).getAddress();
            byte[] start = InetAddress.getByName(startIp).getAddress();
            byte[] end = InetAddress.getByName(endIp).getAddress();
            for (int i = 0; i < target.length; i++) {
                int a = target[i] & 0xFF;
                int s = start[i] & 0xFF;
                int e = end[i] & 0xFF;
                if (a < s || a > e) {
                    return false;
                }
                if (a > s && a < e) {
                    return true;
                }
            }
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static boolean isIpOrCidr(Set<String> entries) {
        for (String entry : entries) {
            if (entry.split("/").length == 1) {
                if (!entry.matches(IPV4) && !entry.matches(IPV6)) {
                    return false;
                }
            } else {
                if (!entry.matches(IPV4_CIDR) && !entry.matches(IPV6_CIDR)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isUnknown(String ip) {
        return ip == null || ip.isEmpty() || "Unknown".equalsIgnoreCase(ip);
    }
}
