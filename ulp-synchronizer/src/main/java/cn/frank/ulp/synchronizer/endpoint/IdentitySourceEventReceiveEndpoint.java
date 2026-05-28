/*
 * ulp-synchronizer - United Login Platform
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
package cn.frank.ulp.synchronizer.endpoint;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.common.entity.identitysource.IdentitySourceEntity;
import cn.frank.ulp.common.repository.identitysource.IdentitySourceRepository;
import cn.frank.ulp.identitysource.core.IdentitySource;
import cn.frank.ulp.identitysource.core.IdentitySourceConfig;
import cn.frank.ulp.support.trace.Trace;
import cn.frank.ulp.synchronizer.configuration.IdentitySourceBeanUtils;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import static cn.frank.ulp.common.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;

/**
 * 身份源回调事件端点
 *
 * @author Frank Zhang
 */
@Slf4j
@RestController
@RequestMapping(EVENT_RECEIVE_PATH)
@SuppressWarnings("unchecked")
public class IdentitySourceEventReceiveEndpoint {

    /**
     * 事件通知处理
     *
     * @param request {@link  HttpServletRequest}
     * @param code {@link  String}
     * @param body {@link  String}
     * @return {@link  ResponseEntity}
     */
    @Trace
    @RequestMapping(value = "/{code}")
    public ResponseEntity<?> receive(HttpServletRequest request, @PathVariable String code,
                                     @RequestBody(required = false) String body) {
        Optional<IdentitySourceEntity> optional = identitySourceRepository.findByCode(code);
        if (optional.isPresent()) {
            IdentitySourceEntity entity = optional.get();
            //已经启用&已经配置
            if (entity.getEnabled() && entity.getConfigured()) {
                String beanName = IdentitySourceBeanUtils.getSourceBeanName(optional.get().getId());
                IdentitySource<IdentitySourceConfig> identitySource = (IdentitySource<IdentitySourceConfig>) applicationContext
                    .getBean(beanName);
                Object event = identitySource.event(request, body);
                return ResponseEntity.ok(event);
            }
        }
        return ResponseEntity.ok().build();
    }

    private final ApplicationContext       applicationContext;

    private final IdentitySourceRepository identitySourceRepository;

    public IdentitySourceEventReceiveEndpoint(ApplicationContext applicationContext,
                                              IdentitySourceRepository identitySourceRepository) {
        this.applicationContext = applicationContext;
        this.identitySourceRepository = identitySourceRepository;
    }
}
