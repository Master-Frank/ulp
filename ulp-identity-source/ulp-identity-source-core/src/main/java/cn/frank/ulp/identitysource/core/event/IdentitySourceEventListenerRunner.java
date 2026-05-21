/*
 * ulp-identity-source-core - United Login Platform
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
package cn.frank.ulp.identitysource.core.event;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 身份源配置事件刷新
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/20 21:45
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnBean(value = IdentitySourceEventListener.class)
public class IdentitySourceEventListenerRunner implements CommandLineRunner, Ordered,
                                               ApplicationContextAware {

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        RTopic topic = redissonClient.getTopic(IdentitySourceEventUtils.IDENTITY_SOURCE_EVENT);
        //添加监听器
        topic.addListener(IdentitySourceEvent.class, (channel, msg) -> {
            //注册
            if (IdentitySourceEventType.REGISTER.equals(msg.getIdentitySourceEventType())) {
                identitySourceEventListener.register(msg.getId());
                return;
            }
            //卸载
            if (IdentitySourceEventType.DESTROY.equals(msg.getIdentitySourceEventType())) {
                identitySourceEventListener.destroy(msg.getId());
            }
            //同步
            if (IdentitySourceEventType.SYNC.equals(msg.getIdentitySourceEventType())) {
                identitySourceEventListener.sync(msg.getId());
            }
        });
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Getter
    private ApplicationContext                applicationContext;

    private final RedissonClient              redissonClient;

    private final IdentitySourceEventListener identitySourceEventListener;

}
