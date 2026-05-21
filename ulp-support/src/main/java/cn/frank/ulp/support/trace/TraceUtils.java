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
package cn.frank.ulp.support.trace;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 跟踪工具类
 * 提供跟踪ID的管理功能
 */
public class TraceUtils {
   public static final String REQUEST_ID = "requestId";

   /**
    * 设置跟踪ID
    *
    * @param traceId 跟踪ID
    */
   public static void put(String traceId) {
      MDC.put("TRACE_ID", traceId);
   }

   /**
    * 移除跟踪ID
    */
   public static void remove() {
      MDC.remove("TRACE_ID");
   }

   /**
    * 获取跟踪ID
    *
    * @return 跟踪ID
    */
   public static String get() {
      return MDC.get("TRACE_ID");
   }

   /**
    * 从请求中获取请求ID
    *
    * @param request HTTP请求
    * @return 请求ID
    */
   public static String getRequestId(HttpServletRequest request) {
      HttpServletRequest req = request;
      String requestId;
      if (StringUtils.isNotBlank(requestId = req.getHeader("trace-id"))) {
         return requestId;
      } else if (StringUtils.isNotBlank(requestId = req.getParameter("requestId"))) {
         return requestId;
      } else {
         return StringUtils.isNotBlank(requestId = req.getParameter("traceId")) ? requestId : req.getParameter("trace_id");
      }
   }
}
