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
package cn.frank.ulp.console.controller.account;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AccountConstants.USER_PATH;

/**
 * 用户身份提供商绑定
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "用户身份提供商绑定关系")
@RestController
@AllArgsConstructor
@RequestMapping(value = USER_PATH + "/idp", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserIdpBindController {

}
