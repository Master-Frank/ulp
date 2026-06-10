/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.web.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.util.StreamUtils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * 可重复请求包装器类
 * 扩展HttpServletRequestWrapper，允许请求体被多次读取
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
    /**
    * 请求体字节数组
    */
    private final byte[] body;

    /**
    * 获取读取器
    *
    * @return 读取器
    * @throws IOException IO异常
    */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
    * 构造函数
    *
    * @param request HTTP请求
    * @param response Servlet响应
    * @throws IOException IO异常
    */
    public RepeatedlyRequestWrapper(HttpServletRequest request,
                                    ServletResponse response) throws IOException {
        super(request);
        request.setCharacterEncoding("UTF-8");
        this.setCharacterEncoding("UTF-8");
        this.body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    /**
    * 获取输入流
    *
    * @return Servlet输入流
    */
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {
            @Override
            public int available() {
                return RepeatedlyRequestWrapper.this.body.length;
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }
        };
    }

    /**
    * 获取参数映射
    *
    * @return 参数映射
    */
    @Override
    public Map<String, String[]> getParameterMap() {
        return super.getParameterMap();
    }
}
