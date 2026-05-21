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
package cn.frank.ulp.console.initializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.StringUtils;

import cn.frank.ulp.common.entity.account.OrganizationMemberEntity;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.OrganizationMemberRepository;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.config.AbstractSystemInitializer;
import cn.frank.ulp.support.config.InitializationException;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_ADMIN_USERNAME;
import static cn.frank.ulp.support.constant.EiamConstants.ROOT_NODE;

/**
 * DefaultAdministratorInitialize
 *
 * @author Frank Zhang
 */
@Component
public class DefaultAdministratorInitializer extends AbstractSystemInitializer {

    private final Logger        logger                      = LoggerFactory
        .getLogger(DefaultAdministratorInitializer.class);
    private static final String DIR_NAME                    = ".ulp";
    private static final String USER_HOME                   = "user.home";
    private static final String INITIAL_PASSWORD_VALUE_NAME = "initial.password.value";
    // generate: 自动生成，setting: 读取[INITIAL_PASSWORD_VALUE]设置的固定值，没有设置使用[INITIAL_PASSWORD_DEFAULT]的默认值
    private static final String INITIAL_PASSWORD_TYPE_NAME  = "initial.password.type";
    private static final String INITIAL_PASSWORD_DEFAULT    = "admin@ulp";

    @Override
    public void init() throws InitializationException {
        try {
            Optional<AdministratorEntity> optional = administratorRepository
                .findByUsername(DEFAULT_ADMIN_USERNAME);
            if (optional.isEmpty()) {
                String initPassword;
                String initPasswordFileTips;
                String passwordType = System.getProperty(INITIAL_PASSWORD_TYPE_NAME);
                if (StringUtils.hasText(passwordType) && "generate".equals(passwordType)) {
                    initPassword = idGenerator.generateId().toString().replace("-", "")
                        .toLowerCase(Locale.ENGLISH);
                } else {
                    String passwordInitial = System.getProperty(INITIAL_PASSWORD_VALUE_NAME);
                    if (StringUtils.hasText(passwordInitial)) {
                        initPassword = passwordInitial;
                    } else {
                        initPassword = INITIAL_PASSWORD_DEFAULT;
                    }
                }
                String initialAdminPasswordFilePath = getInitialAdminPasswordFilePath();
                FileUtils.writeStringToFile(new File(initialAdminPasswordFilePath), initPassword,
                    "UTF-8");
                BufferedWriter stream = new BufferedWriter(
                    new FileWriter(initialAdminPasswordFilePath));
                initPasswordFileTips = "This may also be found at: " + initialAdminPasswordFilePath;
                //清空
                stream.write(initPassword);
                stream.flush();
                stream.close();
                logger.info(
                    """

                            *************************************************************
                            *************************************************************
                            *************************************************************

                            ULP console initial setup is required. An admin administrator has been created and a initialize password.
                            Please use the following password to proceed to installation:

                            %s

                            %s\s
                            *************************************************************
                            *************************************************************
                            *************************************************************

                            """
                        .formatted(initPassword, initPasswordFileTips));
                //保存管理员
                AdministratorEntity administrator = new AdministratorEntity();
                administrator.setUsername(DEFAULT_ADMIN_USERNAME);
                administrator.setFullName("管理员");
                administrator.setPassword(passwordEncoder.encode(initPassword));
                administrator.setStatus(UserStatus.ENABLED);
                administrator.setNeedChangePassword(true);
                administrator.setRemark(
                    "This administrator is automatically created during system initialization.");
                administratorRepository.save(administrator);
                OrganizationMemberEntity member = new OrganizationMemberEntity();
                member.setOrgId(ROOT_NODE);
                member.setUserId(administrator.getId());
                organizationMemberRepository.save(member);
            }
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }

    public static String addSeparator(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }

    /**
     * 获取初始化管理员密码文件路径
     *
     * @return {@link String}
     */
    public static String getInitialAdminPasswordFilePath() {
        String path = addSeparator(System.getProperty(USER_HOME)) + DIR_NAME + File.separator
                      + "secrets" + File.separator;
        return path + "initialAdminPassword";
    }

    @Override
    public int getOrder() {
        return 3;
    }

    private final AlternativeJdkIdGenerator    idGenerator = new AlternativeJdkIdGenerator();

    private final OrganizationMemberRepository organizationMemberRepository;

    private final AdministratorRepository      administratorRepository;

    private final PasswordEncoder              passwordEncoder;

    public DefaultAdministratorInitializer(OrganizationMemberRepository organizationMemberRepository,
                                           AdministratorRepository administratorRepository,
                                           PasswordEncoder passwordEncoder) {
        this.organizationMemberRepository = organizationMemberRepository;
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
    }
}
