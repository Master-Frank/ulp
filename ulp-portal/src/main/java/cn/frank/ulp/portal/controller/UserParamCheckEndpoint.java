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
package cn.frank.ulp.portal.controller;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.CheckValidityType;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.core.security.util.UserUtils;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.util.PhoneUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 当前用户参数校验（GET /api/v1/user/param_check）
 *
 * 用户在个人中心修改邮箱 / 手机号 / 用户名前，前端调用此接口判断目标值是否已被
 * 其他账户占用。校验只针对当前登录用户自身，不接受外部传入的 id。
 *
 * @author Frank Zhang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserParamCheckEndpoint {

    private final UserRepository userRepository;

    @GetMapping("/api/v1/user/param_check")
    public ApiRestResult<Boolean> paramCheck(@NotNull(message = "验证类型不能为空") CheckValidityType type,
                                             @NotEmpty(message = "验证值不能为空") @RequestParam("value") String value) {
        UserEntity self = UserUtils.getUser();

        return switch (type) {
            case PHONE -> ApiRestResult.ok(checkPhone(value, self));
            case EMAIL -> ApiRestResult.ok(checkEmail(value, self));
            case USERNAME -> ApiRestResult.ok(checkUsername(value, self));
            default -> ApiRestResult.ok(Boolean.TRUE);
        };
    }

    private Boolean checkPhone(String value, UserEntity self) {
        try {
            String stripped = value.replace(PhoneUtils.PLUS_SIGN, "");
            if (StringUtils.equals(stripped, StringUtils.defaultString(self.getPhoneAreaCode())
                                             + StringUtils.defaultString(self.getPhone()))) {
                return Boolean.TRUE;
            }
            Phonenumber.PhoneNumber parsed = PhoneNumberUtil.getInstance().parse(value, "CN");
            String national = String.valueOf(parsed.getNationalNumber());
            Optional<UserEntity> existing = userRepository.findByPhone(national);
            return existing.isEmpty() || existing.get().getId().equals(self.getId());
        } catch (NumberParseException e) {
            log.error("校验手机号发生异常", e);
            throw new UlpException("校验手机号发生异常");
        }
    }

    private Boolean checkEmail(String value, UserEntity self) {
        if (StringUtils.equalsIgnoreCase(value, self.getEmail())) {
            return Boolean.TRUE;
        }
        Optional<UserEntity> existing = userRepository.findByEmail(value);
        return existing.isEmpty() || existing.get().getId().equals(self.getId());
    }

    private Boolean checkUsername(String value, UserEntity self) {
        if (StringUtils.equals(value, self.getUsername())) {
            return Boolean.TRUE;
        }
        Optional<UserEntity> existing = userRepository.findByUsername(value);
        return existing.isEmpty() || existing.get().getId().equals(self.getId());
    }
}
