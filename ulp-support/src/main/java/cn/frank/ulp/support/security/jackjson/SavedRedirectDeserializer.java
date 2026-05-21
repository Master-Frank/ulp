/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.security.jackjson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import cn.frank.ulp.support.security.savedredirect.SavedRedirect;
import cn.frank.ulp.support.security.savedredirect.SavedRedirect.Parameter;

public class SavedRedirectDeserializer extends JsonDeserializer<SavedRedirect> {

    @Override
    public SavedRedirect deserialize(JsonParser jp,
                                     DeserializationContext ctxt) throws IOException {
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
