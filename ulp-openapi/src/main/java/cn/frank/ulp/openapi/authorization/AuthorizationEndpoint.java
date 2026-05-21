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
package cn.frank.ulp.openapi.authorization;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.repository.app.AppRepository;
import cn.frank.ulp.openapi.authorization.store.AccessTokenStore;
import cn.frank.ulp.openapi.constant.OpenApiStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.openapi.constant.OpenApiStatus.*;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.ACCESS_TOKEN_EXPIRES_IN;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.AUTH_PATH;

/**
 * 获取 access_token 端点
 *
 * 实现OAuth2协议客户端模式
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/23 07:30
 */
@Tag(name = "访问凭证")
@RestController
@RequestMapping(value = AUTH_PATH)
public class AuthorizationEndpoint {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationEndpoint.class);

    /**
     * 获取access_token
     *
     * @return {@link GetAccessTokenResponse}
     */
    @Operation(summary = "获取 access_token")
    @PostMapping(value = "/access_token", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public GetAccessTokenResponse getAccessToken(@RequestBody(required = false) GetAccessTokenRequest request) {
        GetAccessTokenResponse response = new GetAccessTokenResponse();
        if (ObjectUtils.isEmpty(request)) {
            request = new GetAccessTokenRequest();
        }
        if (StringUtils.isBlank(request.getClientId())) {
            logger.error("客户端ID [{}] 获取 access_token 失败, 客户端ID为空", request.getClientId());
            response.setCode(INVALID_CLIENT_ID_OR_SECRET.getCode());
            response.setMsg(INVALID_CLIENT_ID_OR_SECRET.getDesc());
            return response;
        }
        if (StringUtils.isBlank(request.getClientSecret())) {
            logger.error("客户端ID [{}] 获取 access_token 失败, 客户端秘钥为空", request.getClientId());
            response.setCode(INVALID_CLIENT_ID_OR_SECRET.getCode());
            response.setMsg(INVALID_CLIENT_ID_OR_SECRET.getDesc());
            return response;
        }
        Optional<AppEntity> optionalApp = appRepository.findByClientId(request.getClientId());
        if (optionalApp.isEmpty()) {
            logger.error("客户端ID [{}] 获取 access_token 失败, 根据客户端ID未获取到应用", request.getClientId());
            response.setCode(INVALID_CLIENT_ID_OR_SECRET.getCode());
            response.setMsg(INVALID_CLIENT_ID_OR_SECRET.getDesc());
            return response;
        }
        AppEntity app = optionalApp.get();
        // 校验 client_secret 是否正确
        if (!StringUtils.equals(request.getClientSecret(), app.getClientSecret())) {
            response.setCode(INVALID_CLIENT_ID_OR_SECRET.getCode());
            response.setMsg(INVALID_CLIENT_ID_OR_SECRET.getDesc());
            logger.error("客户端ID [{}] 获取 access_token 失败, 秘钥不正确", request.getClientId());
            return response;
        }
        if (!app.getEnabled()) {
            logger.error("客户端ID [{}] 获取 access_token 失败, 应用未启用", request.getClientId());
            response.setCode(CLIENT_UNAUTHORIZED.getCode());
            response.setMsg(CLIENT_UNAUTHORIZED.getDesc());
            return response;
        }
        AccessToken accessToken = accessTokenStore.findByClientId(request.getClientId());
        response.setCode(OpenApiStatus.SUCCESS.getCode());
        response.setMsg(OpenApiStatus.SUCCESS.getDesc());
        //使用已有的 access_token
        if (!Objects.isNull(accessToken)) {
            response.setAccessToken(accessToken.getValue());
            response.setExpiresIn(ACCESS_TOKEN_EXPIRES_IN);
        }
        //创建 access_token
        else {
            String key = accessTokenGenerator.generateKey();
            response.setAccessToken(key);
            response.setExpiresIn(ACCESS_TOKEN_EXPIRES_IN);
            //放入缓存
            accessTokenStore.save(new AccessToken(request.getClientId(), response.getAccessToken(),
                response.getExpiresIn()));
        }

        logger.info("客户端ID [{}] 获取 access_token 成功: {}", request.getClientId(),
            JSONObject.toJSONString(response));
        return response;
    }

    /**
     * AppRepository
     */
    private final AppRepository      appRepository;

    /**
     * TokenStore
     */
    private final AccessTokenStore   accessTokenStore;

    /**
     * StringKeyGenerator
     */
    private final StringKeyGenerator accessTokenGenerator = new Base64StringKeyGenerator(
        Base64.getUrlEncoder().withoutPadding(), 96);

    public AuthorizationEndpoint(AppRepository appRepository, AccessTokenStore accessTokenStore) {
        this.appRepository = appRepository;
        this.accessTokenStore = accessTokenStore;
    }

}
