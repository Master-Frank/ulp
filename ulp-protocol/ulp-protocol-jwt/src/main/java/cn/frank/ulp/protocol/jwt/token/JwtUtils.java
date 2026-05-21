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

import java.io.IOException;
import java.security.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.frank.ulp.support.util.CertUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.jsonwebtoken.*;

/**
 * JWT 工具类
 *
 * @author Frank Zhang
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * parserToken
     *
     * @param token {@link String}
     * @param publicKey  {@link String}
     * @return {@link Claims}
     */
    public static Claims parserToken(String token, String publicKey) {
        try {
            PublicKey readPublicKey = CertUtils.readPublicKey(publicKey, "");
            JwtParser jwtParser = Jwts.parser().verifyWith(readPublicKey).build();
            // 解析 JWT
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT signature.");
            logger.trace("Invalid JWT signature trace: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            logger.trace("Expired JWT token trace: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
            logger.trace("Unsupported JWT token trace: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
            logger.trace("JWT token compact of handler are invalid trace: {}", e.getMessage());
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
