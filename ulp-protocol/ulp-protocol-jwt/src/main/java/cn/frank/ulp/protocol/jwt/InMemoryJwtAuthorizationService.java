/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt;

import cn.frank.ulp.application.ApplicationServiceLoader;

/**
 * 内存
 *
 * @author Frank Zhang
 */
public class InMemoryJwtAuthorizationService extends AbstractJwtAuthorizationService {

    public InMemoryJwtAuthorizationService(ApplicationServiceLoader applicationServiceLoader) {
        super(applicationServiceLoader);
    }

    @Override
    public void save(JwtAuthentication token) {

    }

    @Override
    public void remove(JwtAuthentication authorization) {

    }

    @Override
    public JwtAuthentication findById(String id) {
        return null;
    }

    @Override
    public JwtAuthentication findByToken(String token) {
        return null;
    }

}
