/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.jackjson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import cn.topiam.employee.support.security.savedredirect.SavedRedirect;
import cn.topiam.employee.support.security.savedredirect.SavedRedirect.Parameter;

public class SavedRedirectDeserializer extends JsonDeserializer<SavedRedirect> {

    @Override
    public SavedRedirect deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        SavedRedirect savedRedirect = new SavedRedirect();
        JsonNode actionNode = node.get("action");
        if (actionNode != null && !actionNode.isNull()) {
            savedRedirect.setAction(actionNode.asText());
        }
        JsonNode methodNode = node.get("method");
        if (methodNode != null && !methodNode.isNull()) {
            savedRedirect.setMethod(methodNode.asText());
        }
        JsonNode parametersNode = node.get("parameters");
        if (parametersNode != null && parametersNode.isArray()) {
            List<Parameter> parameters = new ArrayList<>();
            Iterator<JsonNode> it = parametersNode.elements();
            while (it.hasNext()) {
                JsonNode paramNode = it.next();
                Parameter parameter = new Parameter();
                JsonNode keyNode = paramNode.get("key");
                JsonNode valueNode = paramNode.get("value");
                if (keyNode != null && !keyNode.isNull()) {
                    parameter.setKey(keyNode.asText());
                }
                if (valueNode != null && !valueNode.isNull()) {
                    parameter.setValue(valueNode.asText());
                }
                parameters.add(parameter);
            }
            savedRedirect.setParameters(parameters);
        }
        return savedRedirect;
    }
}
