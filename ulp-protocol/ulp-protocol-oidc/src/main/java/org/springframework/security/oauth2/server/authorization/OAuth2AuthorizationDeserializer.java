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
package org.springframework.security.oauth2.server.authorization;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.node.MissingNode;
import static org.springframework.security.oauth2.server.authorization.OAuth2Authorization.*;

/**
 * OAuth2AuthorizationDeserializer
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationDeserializer extends ValueDeserializer<OAuth2Authorization> {

    private static final TypeReference<Set<String>>                     SET_TYPE_REFERENCE        = new TypeReference<>() {
                                                                                                  };

    private static final TypeReference<Map<String, Object>>             ATTRIBUTES_REFERENCE      = new TypeReference<>() {
                                                                                                  };

    private static final TypeReference<Map<String, Token<OAuth2Token>>> OAUTH2_TOKEN_REFERENCE    = new TypeReference<>() {
                                                                                                  };

    private static final TypeReference<AuthorizationGrantType>          GRANT_TYPE_TYPE_REFERENCE = new TypeReference<>() {
                                                                                                  };

    @Override
    public OAuth2Authorization deserialize(JsonParser jp,
                                           DeserializationContext ctxt) throws JacksonException {
        JsonNode jsonNode = ctxt.readTree(jp);

        JavaType setType = ctxt.getTypeFactory().constructType(SET_TYPE_REFERENCE);
        JavaType attrsType = ctxt.getTypeFactory().constructType(ATTRIBUTES_REFERENCE);
        JavaType tokensType = ctxt.getTypeFactory().constructType(OAUTH2_TOKEN_REFERENCE);
        JavaType grantTypeType = ctxt.getTypeFactory().constructType(GRANT_TYPE_TYPE_REFERENCE);

        Set<String> authorizedScopes = treeAs(ctxt, jsonNode.get("authorizedScopes"), setType);
        Map<String, Object> attributes = treeAs(ctxt, jsonNode.get("attributes"), attrsType);
        Map<String, Token<OAuth2Token>> tokens = treeAs(ctxt, jsonNode.get("tokens"), tokensType);
        AuthorizationGrantType grantType = treeAs(ctxt, jsonNode.get("authorizationGrantType"),
            grantTypeType);

        String id = readJsonNode(jsonNode, "id").asText();
        String registeredClientId = readJsonNode(jsonNode, "registeredClientId").asText();
        String principalName = readJsonNode(jsonNode, "principalName").asText();
        Builder builder = new Builder(registeredClientId).id(id).principalName(principalName)
            .authorizationGrantType(grantType).authorizedScopes(authorizedScopes)
            .attributes(map -> map.putAll(attributes));

        Optional.ofNullable(tokens.get(OAuth2AuthorizationCode.class.getName()))
            .ifPresent(token -> addToken(token, builder));
        Optional.ofNullable(tokens.get(OAuth2AccessToken.class.getName()))
            .ifPresent(token -> addToken(token, builder));
        Optional.ofNullable(tokens.get(OAuth2RefreshToken.class.getName()))
            .ifPresent(token -> addToken(token, builder));
        Optional.ofNullable(tokens.get(OidcIdToken.class.getName()))
            .ifPresent(token -> addToken(token, builder));

        return builder.build();
    }

    public void addToken(Token<OAuth2Token> token, Builder builder) {
        builder.token(token.getToken(), map -> map.putAll(token.getMetadata()));
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

    @SuppressWarnings("unchecked")
    private static <T> T treeAs(DeserializationContext ctxt, JsonNode node,
                                JavaType type) throws JacksonException {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }
        return (T) ctxt.readTreeAsValue(node, type);
    }

}
