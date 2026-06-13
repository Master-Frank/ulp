/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.jackjson;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.jackson.SecurityJacksonModules;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cn.frank.ulp.support.geo.jackson.GeoLocationJacksonModule;
import cn.frank.ulp.support.security.jackjson.SecurityJacksonModule;
import cn.frank.ulp.support.web.jackjson.WebJacksonModule;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

/**
 * 支持Jackson3的模块配置类
 * 提供对GeoLocation、Security和Web相关Jackson模块的支持
 */
public class SupportJackson2Module {

    /**
     * 解密方法
     *
     * @param object 需要解密的对象
     * @return 解密后的字符串
     */
    public static String decryptString(Object object) {
        int var1 = 4 << 4 ^ 3;
        int var2 = 3 << 3;
        int var3 = (3 ^ 5) << 4 ^ 3 << 2 ^ 3;
        String str = (String) object;
        int length = str.length();
        char[] chars = new char[length];
        int index = length - (5 >> 2);
        char[] result = chars;
        int var4 = var3;
        int var5 = index;

        for (int var6 = var2; var5 >= 0; var5 = index) {
            int tempIndex = index;
            int charValue = str.charAt(index);
            --index;
            result[tempIndex] = (char) (charValue ^ var6);
            if (index < 0) {
                break;
            }

            int tempIndex2 = index--;
            result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ var4);
        }

        return new String(result);
    }

    /**
     * 获取模块列表（仅包含 Security + 本项目自定义模块，不含默认类型校验器）。
     * Spring Security 7 的 SecurityJacksonModule 需要在外层通过 activateDefaultTyping 启用，
     * 调用方可使用 {@link #objectMapperBuilder(ClassLoader)} 直接拿到已配置好的 builder。
     *
     * @param classLoader 类加载器
     * @return 模块列表
     */
    public static List<JacksonModule> getModules(ClassLoader classLoader) {
        List<JacksonModule> modules = new ArrayList<>(
            SecurityJacksonModules.getModules(classLoader));
        modules.add(new GeoLocationJacksonModule());
        modules.add(new SecurityJacksonModule());
        modules.add(new WebJacksonModule());
        return modules;
    }

    /**
     * 返回已经配置好默认类型校验、Security/Geo/Web 模块的 JsonMapper.Builder。
     * 调用方按需 addModules 后 .build() 即可。
     */
    public static JsonMapper.Builder objectMapperBuilder(ClassLoader classLoader) {
        BasicPolymorphicTypeValidator.Builder validatorBuilder = BasicPolymorphicTypeValidator
            .builder();
        List<JacksonModule> modules = new ArrayList<>(
            SecurityJacksonModules.getModules(classLoader, validatorBuilder));
        modules.add(new GeoLocationJacksonModule());
        modules.add(new SecurityJacksonModule());
        modules.add(new WebJacksonModule());
        // SecurityJacksonModules 只把 Spring Security 自带类加 PTV allowlist。
        // 项目自定义类（UserType / DataOrigin / Group / Organization / Application / WebAuthenticationDetails 等）
        // 走 default typing 时必须显式 allow，否则 Jackson 3 反序列化抛 InvalidTypeIdException。
        // 另: spring-authorization-server 的 OAuth2AuthorizationCode / OAuth2AccessToken 子类、java.time.* 也需 PTV 放行。
        validatorBuilder.allowIfSubTypeIsArray().allowIfSubType("cn.frank.ulp.")
            .allowIfSubType("java.util.").allowIfSubType("java.lang.").allowIfSubType("java.time.")
            .allowIfSubType("org.springframework.security.");
        return JsonMapper.builder().addModules(modules).activateDefaultTyping(
            validatorBuilder.build(), DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    }
}
