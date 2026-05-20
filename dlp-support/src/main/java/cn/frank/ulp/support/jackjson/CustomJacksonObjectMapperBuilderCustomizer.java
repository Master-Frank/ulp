/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.jackjson;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import cn.frank.ulp.support.constant.EiamConstants;

/**
 * 自定义Jackson对象映射构建器自定义器
 * 用于配置日期时间序列化和反序列化器
 */
public class CustomJacksonObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer {
   
   /**
    * 自定义Jackson对象映射构建器
    * 
    * @param builder Jackson对象映射构建器
    */
   public void customize(Jackson2ObjectMapperBuilder builder) {
      // 配置日期时间反序列化器
      JsonDeserializer<?>[] deserializers = new JsonDeserializer[2];
      deserializers[0] = new LocalDateDeserializer(EiamConstants.DEFAULT_DATE_FORMATTER);
      deserializers[1] = new LocalDateTimeDeserializer(EiamConstants.DEFAULT_DATE_TIME_FORMATTER);
      builder.deserializers(deserializers);
      
      // 配置日期时间序列化器
      JsonSerializer<?>[] serializers = new JsonSerializer[2];
      serializers[0] = new LocalDateSerializer(EiamConstants.DEFAULT_DATE_FORMATTER);
      serializers[1] = new LocalDateTimeSerializer(EiamConstants.DEFAULT_DATE_TIME_FORMATTER);
      builder.serializers(serializers);
   }
}
