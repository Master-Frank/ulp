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
package cn.frank.ulp.support.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Apache HttpClient 4.x 包装工具.
 */
@Slf4j
public class HttpClientUtils {

    public HttpClientUtils() {
    }

    public static String get(String url) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet();
            get.setURI(URI.create(url));
            try (CloseableHttpResponse response = client.execute(get)) {
                return readBody(response);
            } finally {
                get.abort();
            }
        }
    }

    public static String get(String url, Map<String, String> params, BasicHeader... headers) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(5000)
                .setConnectionRequestTimeout(1000).setSocketTimeout(60000).build();
            get.setConfig(config);
            if (headers != null) {
                get.setHeaders(headers);
            }
            String query = params != null
                ? org.apache.http.client.utils.URLEncodedUtils.format(toPairs(params),
                    StandardCharsets.UTF_8)
                : "";
            URL u = new URL(url + (query.isEmpty() ? "" : "?" + query));
            URI uri = new URI(u.getProtocol(), u.getHost(), u.getPath(), u.getQuery(), null);
            get.setURI(uri);
            try (CloseableHttpResponse response = client.execute(get)) {
                return readBody(response);
            } finally {
                get.abort();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doGet(String url, Map<String, String> params) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            URIBuilder uri = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, String> e : params.entrySet()) {
                    uri.addParameter(e.getKey(), e.getValue());
                }
            }
            HttpGet get = new HttpGet(uri.build());
            try (CloseableHttpResponse response = client.execute(get)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    return org.apache.http.util.EntityUtils.toString(response.getEntity(),
                        StandardCharsets.UTF_8);
                }
                return "";
            }
        } catch (Exception e) {
            log.error("HTTP GET error", e);
            return "";
        }
    }

    public static String post(String url, String body) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost(url);
            post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
            post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
            try (CloseableHttpResponse response = client.execute(post)) {
                return readBody(response);
            } finally {
                post.abort();
            }
        }
    }

    public static String post(String url, Map<String, String> params) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(toPairs(params), StandardCharsets.UTF_8));
            try (CloseableHttpResponse response = client.execute(post)) {
                return readBody(response);
            } finally {
                post.abort();
            }
        }
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPost(String url, Map<String, String> params) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            if (params != null) {
                List<NameValuePair> pairs = toPairs(params);
                post.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
            }
            try (CloseableHttpResponse response = client.execute(post)) {
                return org.apache.http.util.EntityUtils.toString(response.getEntity(),
                    StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("HTTP POST error", e);
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
    }

    public static String doPostJson(String url, String body) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse response = client.execute(post)) {
                return org.apache.http.util.EntityUtils.toString(response.getEntity(),
                    StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("HTTP POST JSON error", e);
            return "";
        }
    }

    public static String postRequestByFormEntity(String url, UrlEncodedFormEntity formEntity) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setEntity(formEntity);
            try (CloseableHttpResponse response = client.execute(post)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    return org.apache.http.util.EntityUtils.toString(response.getEntity(),
                        StandardCharsets.UTF_8);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    public static String put(String url, Map<String, String> params) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPut put = new HttpPut(url);
            put.setEntity(new UrlEncodedFormEntity(toPairs(params), StandardCharsets.UTF_8));
            try (CloseableHttpResponse response = client.execute(put)) {
                return readBody(response);
            } finally {
                put.abort();
            }
        }
    }

    public static String delete(String url) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpDelete delete = new HttpDelete();
            delete.setURI(URI.create(url));
            try (CloseableHttpResponse response = client.execute(delete)) {
                return readBody(response);
            } finally {
                delete.abort();
            }
        }
    }

    public static String delete(String url, Map<String, String> params) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpDelete delete = new HttpDelete();
            String query = org.apache.http.client.utils.URLEncodedUtils.format(toPairs(params),
                StandardCharsets.UTF_8);
            delete.setURI(URI.create(url + "?" + query));
            try (CloseableHttpResponse response = client.execute(delete)) {
                return readBody(response);
            } finally {
                delete.abort();
            }
        }
    }

    public static String client(String url, HttpMethod method,
                                MultiValueMap<String, String> params) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            return restTemplate.exchange(url, method, entity, String.class).getBody();
        } catch (RestClientException e) {
            log.error("RestTemplate exchange error", e);
            return "";
        }
    }

    public static void outPrint(HttpServletResponse response, Object data) {
        try {
            log.info("<<<<-------outPrint返回数据------->>>> {}", data);
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.getWriter().print(data);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error("outPrint error", e);
        }
    }

    public static UrlEncodedFormEntity buildPairList(Map<String, String> params) {
        try {
            return new UrlEncodedFormEntity(toPairs(params), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    private static List<NameValuePair> toPairs(Map<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                pairs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
            }
        }
        return pairs;
    }

    private static String readBody(CloseableHttpResponse response) throws IOException {
        return org.apache.http.util.EntityUtils.toString(response.getEntity(),
            StandardCharsets.UTF_8);
    }
}
