/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.authorization.store;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import cn.frank.ulp.openapi.authorization.AccessToken;
import static cn.frank.ulp.support.constant.EiamConstants.COLON;

/**
 * RedisTokenStore
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/5/8 21:54
 */
public class RedisAccessTokenStore implements AccessTokenStore {

    /**
     * save
     *
     * @param accessToken {@link AccessToken}
     */
    @Override
    public void save(AccessToken accessToken) {
        //缓存 index
        redisTemplate.opsForValue().set(getIndexKey(accessToken.getClientId()),
            accessToken.getValue(), accessToken.getExpiresIn(), TimeUnit.SECONDS);
        //缓存 access_token
        redisTemplate.opsForValue().set(getAccessTokenKey(accessToken.getValue()), accessToken,
            accessToken.getExpiresIn(), TimeUnit.SECONDS);
    }

    /**
     * 根据token删除
     *
     * @param token {@link String}
     */
    @Override
    public void deleteByToken(String token) {
        AccessToken accessToken = (AccessToken) redisTemplate.opsForValue()
            .get(getAccessTokenKey(token));
        if (!Objects.isNull(accessToken)) {
            //删除index
            redisTemplate.delete(getIndexKey(accessToken.getClientId()));
            //删除access_token
            redisTemplate.delete(getAccessTokenKey(token));
        }
    }

    /**
     * 根据token查询
     *
     * @param token {@link String}
     * @return {@link AccessToken}
     */
    @Override
    public AccessToken findByToken(String token) {
        return (AccessToken) redisTemplate.opsForValue().get(getAccessTokenKey(token));
    }

    /**
     * 根据 clientId 查询
     *
     * @param clientId {@link String} 客户端ID
     * @return {@link List}
     */
    @Override
    public AccessToken findByClientId(String clientId) {
        String index = (String) redisTemplate.opsForValue().get(getIndexKey(clientId));
        if (StringUtils.isNotEmpty(index)) {
            return (AccessToken) redisTemplate.opsForValue().get(getAccessTokenKey(index));
        }
        return null;
    }

    /**
     * RedisTemplate
     */
    private final RedisTemplate<Object, Object> redisTemplate;

    public static final String                  OPENAPI_TOKEN_KEY_PREFIX = "openapi" + COLON
                                                                           + "access_token" + COLON;
    public static final String                  INDEX_KEY_PREFIX         = "openapi" + COLON
                                                                           + "index" + COLON;

    private static String getAccessTokenKey(String key) {
        return OPENAPI_TOKEN_KEY_PREFIX + key;
    }

    private static String getIndexKey(String key) {
        return INDEX_KEY_PREFIX + key;
    }

    public RedisAccessTokenStore(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
