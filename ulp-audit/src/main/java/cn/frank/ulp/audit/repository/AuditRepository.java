/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.audit.entity.AuditEntity;
import cn.frank.ulp.audit.event.type.EventType;

/**
 * 行为审计repository
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/11 22:32
 */
@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, String>,
                                 AuditCustomizedRepository, JpaSpecificationExecutor<AuditEntity> {

    @Query(value = "SELECT COUNT(*) FROM AuditEntity WHERE eventType = :type AND eventTime BETWEEN :startTime AND :endTime")
    Long countByTypeAndTime(@Param("type") EventType type,
                            @Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime);
}
