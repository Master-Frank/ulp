/*
 * ulp-identity-source-dingtalk - United Login Platform
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
package cn.frank.ulp.identitysource.dingtalk;

import org.apache.commons.lang3.StringUtils;

import com.aliyun.dingtalkcontact_1_0.models.GetOrgAuthInfoHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetOrgAuthInfoRequest;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import cn.frank.ulp.identitysource.core.IdentitySourceConfigValidator;
import cn.frank.ulp.identitysource.core.exception.ApiCallException;
import cn.frank.ulp.identitysource.dingtalk.client.AbstractDingTalkClient;
import cn.frank.ulp.support.validation.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

import jakarta.validation.ConstraintViolationException;

/**
 * 钉钉身份源客户端配置验证器
 *
 * @author Frank Zhang
 */
@Slf4j
public class DingTalkConfigValidator implements IdentitySourceConfigValidator<DingTalkConfig> {
    public static final String ACCESS_TOKEN_PERMISSION_DENIED = "Forbidden.AccessDenied.AccessTokenPermissionDenied";

    @Override
    public Boolean validate(DingTalkConfig config) {
        try {
            ValidationUtils.ValidationResult<DingTalkConfig> validationResult = ValidationUtils
                .validateEntity(config);
            if (validationResult.isHasErrors()) {
                log.error("校验钉钉配置失败：{}", validationResult.getMessage());
                throw new ConstraintViolationException(validationResult.getConstraintViolations());
            }
            //获取 AccessToken
            Client client = AbstractDingTalkClient.createClient();
            GetAccessTokenRequest request = new GetAccessTokenRequest()
                .setAppKey(config.getAppKey()).setAppSecret(config.getAppSecret());
            GetAccessTokenResponse accessToken = client.getAccessToken(request);
            //根据 corpId 获取企业信息
            if (StringUtils.isNotBlank(config.getCorpId())) {
                GetOrgAuthInfoHeaders getOrgAuthInfoHeaders = new GetOrgAuthInfoHeaders();
                getOrgAuthInfoHeaders.xAcsDingtalkAccessToken = accessToken.body.getAccessToken();
                GetOrgAuthInfoRequest getOrgAuthInfoRequest = new GetOrgAuthInfoRequest()
                    .setTargetCorpId(config.getCorpId());
                getDingTalkContact().getOrgAuthInfoWithOptions(getOrgAuthInfoRequest,
                    getOrgAuthInfoHeaders, new RuntimeOptions());
            }
            return true;
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code)
                && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("钉钉身份源参数验证发生错误 [CODE: {}, MESSAGE: {}]", err.getCode(), err.getMessage());
                throw new ApiCallException(err.getMessage());
            }
        } catch (Exception exception) {
            TeaException err = new TeaException(exception.getMessage(), exception);
            if (!com.aliyun.teautil.Common.empty(err.code)
                && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("钉钉身份源参数验证发生错误 [CODE: {}, MESSAGE: {}]", err.getCode(), err.getMessage());
                throw new ApiCallException(err.getMessage());
            }
        }
        return false;
    }

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception Exception
     */
    public static com.aliyun.dingtalkcontact_1_0.Client getDingTalkContact() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }
}
