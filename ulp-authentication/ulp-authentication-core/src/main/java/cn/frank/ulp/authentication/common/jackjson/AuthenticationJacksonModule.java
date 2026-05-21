/*
 * ulp-authentication-core - United Login Platform
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
package cn.frank.ulp.authentication.common.jackjson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import cn.frank.ulp.authentication.common.IdentityProviderType;
import cn.frank.ulp.authentication.common.authentication.IdentityProviderAuthentication;
import cn.frank.ulp.authentication.common.authentication.IdentityProviderNotBindAuthentication;
import cn.frank.ulp.authentication.common.authentication.IdentityProviderUserDetails;
import cn.frank.ulp.authentication.common.authentication.OtpAuthentication;

/**
 *
 * @author Frank Zhang
 */
public class AuthenticationJacksonModule extends Module {

    /**
     * Method that returns a display that can be used by Jackson
     * for informational purposes, as well as in associating extensions with
     * module that provides them.
     */
    @Override
    public String getModuleName() {
        return AuthenticationJacksonModule.class.getName();
    }

    /**
     * Method that returns version of this module. Can be used by Jackson for
     * informational purposes.
     */
    @Override
    public Version version() {
        return new Version(1, 0, 0, null, null, null);
    }

    @Override
    public void setupModule(SetupContext context) {
        //@formatter:off
        context.setMixInAnnotations(IdentityProviderAuthentication.class, IdentityProviderAuthenticationTokenMixin.class);
        context.setMixInAnnotations(IdentityProviderNotBindAuthentication.class, IdentityProviderNotBindAuthenticationTokenMixin.class);
        context.setMixInAnnotations(OtpAuthentication.class, OtpAuthenticationTokenMixin.class);
        context.setMixInAnnotations(IdentityProviderUserDetails.class, IdentityProviderUserDetailsMixin.class);
        context.setMixInAnnotations(IdentityProviderType.class, IdentityProviderTypeMixin.class);
        //@formatter:on
    }

}
