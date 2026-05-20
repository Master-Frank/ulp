/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.message.SmsSendRecordEntity;

/**
 * @author TopIAM
 */
@Repository
public interface SmsSendRecordRepository extends JpaRepository<SmsSendRecordEntity, String> {

}
