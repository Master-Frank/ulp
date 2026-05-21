/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.jackjson.encrypt;

import java.util.Objects;

import org.springframework.util.Assert;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.util.AesUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static cn.frank.ulp.common.constant.SettingConstants.AES_SECRET;

/**
 * EncryptContextHelp
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/22 21:53
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptContextHelp {
    private static final AesUtils AES_UTILS = new AesUtils(getAesSecret());

    public static String encrypt(String content) {
        return AES_UTILS.encrypt(content);
    }

    public static String decrypt(String content) {
        if (Objects.isNull(content)) {
            return null;
        }
        return AES_UTILS.decrypt(content);
    }

    /**
     * 获取AES秘钥
     *
     * @return  {@link String}
     */
    public static String getAesSecret() {
        SettingEntity setting = getSettingRepository().findByName(AES_SECRET);
        Assert.notNull(setting, "aes secret must not be null");
        return setting.getValue();
    }

    private static SettingRepository getSettingRepository() {
        return ApplicationContextService.getBean(SettingRepository.class);
    }
}
