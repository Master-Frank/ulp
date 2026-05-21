/*
 * eiam-protocol-form - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.form;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.protocol.form.jackson.FormAuthorizationModule;
import cn.frank.ulp.support.jackjson.SupportJackson2Module;

import lombok.Setter;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 23:30
 */
public class RedisFormAuthorizationService extends AbstractFormAuthorizationService {

    public RedisFormAuthorizationService(RedisOperations<String, String> redisOperations,
                                         AutowireCapableBeanFactory beanFactory,
                                         ApplicationServiceLoader applicationServiceLoader) {
        super(applicationServiceLoader);
        Assert.notNull(redisOperations, "redisOperations mut not be null");
        this.redisOperations = redisOperations;
        ClassLoader classLoader = this.getClass().getClassLoader();
        objectMapper.registerModules(SupportJackson2Module.getModules(classLoader));
        objectMapper.registerModule(new FormAuthorizationModule());
        objectMapper.setHandlerInstantiator(new SpringHandlerInstantiator(beanFactory));
    }

    private final RedisOperations<String, String> redisOperations;

    @Setter
    private ObjectMapper                          objectMapper = new ObjectMapper();
}
