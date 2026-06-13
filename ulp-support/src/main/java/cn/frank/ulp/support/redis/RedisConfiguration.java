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
package cn.frank.ulp.support.redis;

import java.time.Duration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import cn.frank.ulp.support.cache.CachePrefixGenerator;
import cn.frank.ulp.support.cache.UlpCacheProperties;
import cn.frank.ulp.support.util.PhoneUtils;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

/**
 * Redis配置类
 * 用于配置Redis连接和相关Bean
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(UlpCacheProperties.class)
public class RedisConfiguration {

    /**
    * Redis连接工厂Bean
    *
    * @param redisProperties Redis属性
    * @return Redis连接工厂
    */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(DataRedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setDatabase(redisProperties.getDatabase());
        if (redisProperties.getPassword() != null) {
            configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }

        return new LettuceConnectionFactory(configuration);
    }

    /**
    * Redis模板Bean
    *
    * @param connectionFactory Redis连接工厂
    * @return Redis模板
    */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public CachePrefixGenerator cachePrefixGenerator(UlpCacheProperties cacheProperties) {
        String prefix = cacheProperties.getRedis().getKeyPrefix();
        return new CachePrefixGenerator(prefix == null ? "ulp" : prefix);
    }

    /**
    * Redis缓存管理器Bean
    *
    * @param connectionFactory Redis连接工厂
    * @param cachePrefixGenerator 缓存前缀生成器
    * @return Redis缓存管理器
    */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          CachePrefixGenerator cachePrefixGenerator) {
        ObjectMapper objectMapper = JsonMapper.builder()
            .changeDefaultVisibility(
                vc -> vc.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
            .activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
                DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJacksonJsonRedisSerializer(objectMapper)))
            .computePrefixWith(cachePrefixGenerator).disableCachingNullValues();
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
    }

    /**
    * Redisson客户端Bean
    *
    * @param redisProperties Redis属性
    * @return Redisson客户端
    */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(DataRedisProperties redisProperties) {
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        Assert.hasText(host, PhoneUtils.decryptString("\u0001g0`'|6D'`1{-|"));
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        if (StringUtils.hasText(password)) {
            config.useSingleServer().setPassword(password);
        }

        return Redisson.create(config);
    }
}
