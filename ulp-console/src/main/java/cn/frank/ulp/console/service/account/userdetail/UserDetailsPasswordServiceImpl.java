/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.service.account.userdetail;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

import cn.frank.ulp.common.repository.setting.AdministratorRepository;

/**
 *
 * @author Frank Zhang
 */
@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

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
        administratorRepository.updatePassword(
            ((cn.frank.ulp.support.security.userdetails.UserDetails) user).getId(), newPassword,
            LocalDateTime.now());
        return user;
    }

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    public UserDetailsPasswordServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }
}
