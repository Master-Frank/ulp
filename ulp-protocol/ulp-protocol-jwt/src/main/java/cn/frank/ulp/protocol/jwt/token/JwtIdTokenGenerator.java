/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.token;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;

import cn.frank.ulp.protocol.jwt.exception.IdTokenGenerateException;
import cn.frank.ulp.support.util.CertUtils;

import io.jsonwebtoken.Jwts;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/10 21:00
 */
public class JwtIdTokenGenerator implements IdTokenGenerator {

    @Override
    public IdToken generate(IdTokenContext context) {
        //@formatter:off
        try{
            Instant issuedAt = Instant.now();
            Instant expiresAt=issuedAt.plus(context.getIdTokenTimeToLive());
            // 生成私钥
            PrivateKey rsaPrivateKey = CertUtils.readPrivateKey(context.getPrivateKey(), "");
            // 生成 JWT 令牌
            String tokenValue = Jwts.builder().issuer(context.getIssuer())
                    .issuedAt(new Date(issuedAt.toEpochMilli()))
                    .subject(context.getSubject())
                    .audience().add(context.getAudience()).and()
                    .expiration(new Date(expiresAt.toEpochMilli()))
                    .signWith(rsaPrivateKey, Jwts.SIG.RS256)
                    .compact();
            return IdToken.builder().tokenValue(tokenValue)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .build();
        }catch (Exception e){
            throw new IdTokenGenerateException(e);
        }
        //@formatter:off
    }
}
