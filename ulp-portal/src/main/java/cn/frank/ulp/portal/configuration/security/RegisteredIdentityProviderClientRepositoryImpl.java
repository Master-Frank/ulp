/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.configuration.security;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.authentication.common.client.IdentityProviderConfig;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClient;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.common.entity.authn.IdentityProviderEntity;
import cn.frank.ulp.common.repository.authentication.IdentityProviderRepository;

/**
 * @author Frank Zhang
 */
@Component
public class RegisteredIdentityProviderClientRepositoryImpl implements
                                                            RegisteredIdentityProviderClientRepository {

    /**
     * 根据code查询身份提供商配置
     *
     * @param code {@link String}
     * @return {@link RegisteredIdentityProviderClient}
     */
    @Override
    public <T extends IdentityProviderConfig> Optional<RegisteredIdentityProviderClient<T>> findByCode(String code) {
        //@formatter:off
        Optional<IdentityProviderEntity> optional = identityProviderRepository.findByCodeAndEnabledIsTrue(code);
        if (optional.isPresent()) {
            try {
                // 指定序列化输入的类型
                ObjectMapper objectMapper=new ObjectMapper();
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                IdentityProviderEntity entity = optional.get();
                T provider = objectMapper.readValue(entity.getConfig(), new TypeReference<>() {});
                RegisteredIdentityProviderClient<T> client = RegisteredIdentityProviderClient.<T>builder()
                        .id(String.valueOf(entity.getId()))
                        .code(entity.getCode())
                        .name(entity.getName())
                        .config(provider)
                        .build();
                return Optional.of(client);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //@formatter:on
        return Optional.empty();
    }

    private final IdentityProviderRepository identityProviderRepository;

    public RegisteredIdentityProviderClientRepositoryImpl(IdentityProviderRepository identityProviderRepository) {
        this.identityProviderRepository = identityProviderRepository;
    }

}
