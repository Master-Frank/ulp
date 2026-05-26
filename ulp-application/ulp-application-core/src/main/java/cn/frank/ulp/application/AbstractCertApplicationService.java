/*
 * ulp-application-core - United Login Platform
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
package cn.frank.ulp.application;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.common.entity.app.AppCertEntity;
import cn.frank.ulp.common.enums.app.AppCertUsingType;
import cn.frank.ulp.common.exception.app.AppCreateCertException;
import cn.frank.ulp.common.repository.app.*;
import cn.frank.ulp.support.util.CertUtils;
import cn.frank.ulp.support.util.RsaUtils;
import static cn.frank.ulp.support.util.CertUtils.encodePem;
import static cn.frank.ulp.support.util.CertUtils.getX500Name;
import static cn.frank.ulp.support.util.RsaUtils.getKeys;

/**
 * AbstractCertificateApplicationService
 *
 * @author Frank Zhang
 */
public abstract class AbstractCertApplicationService extends AbstractApplicationService {
    private final Logger         logger = LoggerFactory
        .getLogger(AbstractCertApplicationService.class);
    protected final ObjectMapper mapper = new ObjectMapper();

    /**
     * 创建证书
     *
     * @param appId     {@link String}
     * @param appCode     {@link String}
     * @param usingType {@link AppCertUsingType}
     */
    public void createCertificate(String appId, String appCode, AppCertUsingType usingType) {
        try {
            AppCertEntity config = new AppCertEntity();
            config.setAppId(appId);
            //私钥长度
            config.setKeyLong(2048);
            //算法
            config.setSignAlgo("SHA256WITHRSA");
            RsaUtils.RsaResult keys = getKeys(config.getKeyLong());
            X500Name x500Name = getX500Name("app_" + appCode, "ULP", "Jinan", "Shandong", "CN",
                "ULP");
            //发行者
            config.setIssuer(x500Name.toString());
            //主题
            config.setSubject(x500Name.toString());
            //证书 起始日期 与 结束日期
            LocalDateTime localDateTime = LocalDateTime.now();
            //证书序列号
            config.setSerial(BigInteger.valueOf(System.currentTimeMillis()));
            //开始时间
            Date notBefore = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            config.setBeginDate(
                notBefore.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            //结束时间
            Date notAfter = Date
                .from(localDateTime.plusYears(10).atZone(ZoneId.systemDefault()).toInstant());
            config
                .setEndDate(notAfter.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            //相差天数
            config
                .setValidity((int) ((notAfter.getTime() - notBefore.getTime()) / 1000 / 3600 / 24));
            //生成证书
            String certificate = CertUtils.getCertificate(x500Name, x500Name, config.getSerial(),
                notBefore, notAfter, keys.getPublicKey(), keys.getPrivateKey());
            //私钥
            config.setPrivateKey(encodePem(keys.getPrivateKey()));
            //公钥
            config.setPublicKey(encodePem(keys.getPublicKey()));
            //证书
            config.setCert(certificate);
            //使用类型
            config.setUsingType(usingType);
            appCertRepository.save(config);
        } catch (Exception e) {
            logger.error("创建应用证书异常", e);
            throw new AppCreateCertException();
        }
    }

    /**
     * AppCertRepository
     */
    protected final AppCertRepository         appCertRepository;

    /**
     *AppAccessPolicyRepository
     */
    protected final AppAccessPolicyRepository appAccessPolicyRepository;

    /**
     * IdGenerator
     */
    protected final IdGenerator               idGenerator;

    protected AbstractCertApplicationService(AppCertRepository appCertRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppAccessPolicyRepository appAccessPolicyRepository,
                                             AppRepository appRepository) {
        super(appAccountRepository, appGroupAssociationRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appAccessPolicyRepository = appAccessPolicyRepository;
        this.idGenerator = new AlternativeJdkIdGenerator();
    }
}
