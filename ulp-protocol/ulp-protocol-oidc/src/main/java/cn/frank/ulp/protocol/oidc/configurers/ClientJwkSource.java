/*
 * ulp-protocol-oidc - United Login Platform
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
package cn.frank.ulp.protocol.oidc.configurers;

import java.util.List;
import java.util.Map;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import cn.frank.ulp.application.context.ApplicationContext;
import cn.frank.ulp.application.context.ApplicationContextHolder;
import cn.frank.ulp.application.oidc.model.OidcProtocolConfig;

/**
 * 客户端JWK
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/27 22:50
 */
public class ClientJwkSource implements JWKSource<SecurityContext> {

    @Override
    public List<JWK> get(JWKSelector jwkSelector,
                         SecurityContext context) throws KeySourceException {
        //根据应用编码获取应用证书
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        Map<String, Object> config = applicationContext.getConfig();
        OidcProtocolConfig protocolConfig = (OidcProtocolConfig) config
            .get(OidcProtocolConfig.class.getName());
        return jwkSelector.select(new JWKSet(protocolConfig.getJwks()));
    }
}
