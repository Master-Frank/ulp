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
package cn.frank.ulp.common.repository.identitysource.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.identitysource.IdentitySourceSyncRecordEntity;
import cn.frank.ulp.common.repository.identitysource.IdentitySourceSyncRecordRepositoryCustomized;

/**
 * 身份源同步记录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/15 21:35
 */
@Repository
public class IdentitySourceSyncRecordRepositoryCustomizedImpl implements
                                                              IdentitySourceSyncRecordRepositoryCustomized {

    @Override
    public void batchSave(List<IdentitySourceSyncRecordEntity> list) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO eiam_identity_source_sync_record (id_, sync_history_id, action_type, object_id, object_name, object_type, status_,desc_,create_by,create_time,update_by,update_time,remark_,is_deleted) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
            //@formatter:off
                    IdentitySourceSyncRecordEntity entity = list.get(i);
                    ps.setString(1, entity.getId());
                    ps.setString(2, entity.getSyncHistoryId());
                    ps.setString(3, Objects.isNull(entity.getActionType()) ? null : entity.getActionType().getCode());
                    ps.setString(4, entity.getObjectId());
                    ps.setString(5, entity.getObjectName());
                    ps.setString(6, Objects.isNull(entity.getObjectType()) ? null : entity.getObjectType().getCode());
                    ps.setString(7, Objects.isNull(entity.getStatus()) ? null : entity.getStatus().getCode());
                    ps.setString(8, entity.getDesc());
                    ps.setString(9, entity.getCreateBy());
                    ps.setTimestamp(10, Timestamp.valueOf(entity.getCreateTime()));
                    ps.setString(11, entity.getUpdateBy());
                    ps.setTimestamp(12, Timestamp.valueOf(entity.getUpdateTime()));
                    ps.setString(13, entity.getRemark());
                    ps.setBoolean(14, false);
                    //@formatter:on
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
    }

    private final JdbcTemplate jdbcTemplate;

    public IdentitySourceSyncRecordRepositoryCustomizedImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
