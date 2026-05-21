/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.geo.maxmind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.frank.ulp.support.geo.GeoLocationParser;

import lombok.RequiredArgsConstructor;

/**
 * 更新 maxmind 数据库
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/7 22:25
 */
@Component
@RequiredArgsConstructor
public class UpdateMaxmindTaskConfiguration {
    private final Logger logger = LoggerFactory.getLogger(UpdateMaxmindTaskConfiguration.class);

    /**
     * 每天一点执行ip库文件更新
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void update() {
        try {
            if (geoLocationParser instanceof MaxmindGeoLocationParserImpl maxmindGeoLocation) {
                logger.info("执行IP库文件更新定时任务开始");
                if (maxmindGeoLocation.checkDbFileIsUpdate()) {
                    maxmindGeoLocation.download();
                }
                logger.info("执行IP库文件更新定时任务结束");
            }
        } catch (Exception e) {
            logger.error("执行IP库文件更新定时发生异常：{}", e.getMessage());
        }
    }

    private final GeoLocationParser geoLocationParser;

}
