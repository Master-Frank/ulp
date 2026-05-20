/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.jackjson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import cn.topiam.employee.support.security.core.GrantedAuthority;

public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {

    @Override
    public GrantedAuthority deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode typeNode = node.get("type");
        JsonNode authorityNode = node.get("authority");
        String type = typeNode == null || typeNode.isNull() ? "" : typeNode.asText();
        String authority = authorityNode == null || authorityNode.isNull() ? "" : authorityNode.asText();
        return new GrantedAuthority(type, authority);
    }
}
