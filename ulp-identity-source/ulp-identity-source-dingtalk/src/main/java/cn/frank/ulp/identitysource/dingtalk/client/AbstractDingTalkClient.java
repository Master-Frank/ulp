/*
 * ulp-identity-source-dingtalk - United Login Platform
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
package cn.frank.ulp.identitysource.dingtalk.client;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;

import com.alibaba.fastjson2.JSON;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cn.frank.ulp.identitysource.core.client.AbstractIdentitySourceClient;
import cn.frank.ulp.identitysource.core.exception.ApiCallException;
import cn.frank.ulp.identitysource.dingtalk.DingTalkConfig;

import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.identitysource.dingtalk.DingTalkConstants.LOGGER_NAME;

/**
 * AbstractDingTalkDataProcessor
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 03:40
 */
@Slf4j(topic = LOGGER_NAME)
public abstract class AbstractDingTalkClient extends AbstractIdentitySourceClient<DingTalkConfig> {
    /**
     * 缓存
     */
    private Cache<String, String> cache;

    protected AbstractDingTalkClient(DingTalkConfig config) {
        super(config);
        String token = getAccessToken();
        cache.put(ACCESS_KEY, token);
    }

    /**
     * 获取访问令牌
     *
     * @return {@link String}
     */
    protected String getAccessToken() {
        try {
            DingTalkConfig config = getConfig();
            if (ObjectUtils.isNotEmpty(cache)
                && ObjectUtils.isNotEmpty(cache.getIfPresent(ACCESS_KEY))) {
                return cache.getIfPresent(ACCESS_KEY);
            }
            Client client = createClient();
            GetAccessTokenRequest request = new GetAccessTokenRequest()
                .setAppKey(config.getAppKey()).setAppSecret(config.getAppSecret());
            log.debug("获取钉钉 Access Token 入参: {}", JSON.toJSONString(request));
            GetAccessTokenResponse accessToken = client.getAccessToken(request);
            log.debug("获取钉钉 Access Token 返回: {}", JSON.toJSONString(accessToken));
            GetAccessTokenResponseBody body = accessToken.getBody();
            cache = CacheBuilder.newBuilder()
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .expireAfterWrite(body.getExpireIn(), TimeUnit.SECONDS).build();
            cache.put(ACCESS_KEY, body.getAccessToken());
            return cache.getIfPresent(ACCESS_KEY);
        } catch (Exception e) {
            log.error("获取钉钉 Access Token 失败: {}", e.getMessage());
            throw new ApiCallException("获取钉钉 Access Token 失败");
        }
    }

    /**
     * 使用 Token 初始化账号Client
     */
    public static Client createClient() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            return new Client(config);
        } catch (Exception e) {
            log.error("钉钉获取Client异常：{}", e.getMessage());
            throw new ApiCallException(e.getMessage());
        }
    }

}
