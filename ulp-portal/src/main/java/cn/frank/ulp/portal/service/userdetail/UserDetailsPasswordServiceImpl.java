/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.service.userdetail;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

import cn.frank.ulp.common.repository.account.UserRepository;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/10/19 14:10
 */
@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailsPasswordService.class);

    /**
     * Modify the specified user's password. This should change the user's password in the
     * persistent user repository (database, LDAP etc).
     *
     * @param user        the user to modify the password for
     * @param newPassword the password to change to, encoded by the configured
     *                    {@code PasswordEncoder}
     * @return the updated UserDetails with the new password
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        String userId = ((cn.frank.ulp.support.security.userdetails.UserDetails) user)
            .getId();
        userRepository.updatePassword(userId, newPassword, LocalDateTime.now());
        return user;
    }

    /**
     * UserRepository
     */
    private final UserRepository userRepository;

    public UserDetailsPasswordServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
