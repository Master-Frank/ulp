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
package cn.frank.ulp.common.repository.account.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.account.OrganizationMemberEntity;
import cn.frank.ulp.common.repository.account.OrganizationMemberCustomizedRepository;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/2 02:54
 */
@Repository
@RequiredArgsConstructor
public class OrganizationMemberCustomizedRepositoryImpl implements
                                                        OrganizationMemberCustomizedRepository {

    @Override
    public void batchSave(List<OrganizationMemberEntity> list) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO eiam_organization_member (id_, org_id, user_id, create_by, create_time, update_by, update_time, remark_, is_deleted) VALUES (?,?,?,?,?,?,?,?,?)",
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                    OrganizationMemberEntity entity = list.get(i);
                    ps.setString(1,
                        entity.getId() != null ? entity.getId() : UUID.randomUUID().toString());
                    ps.setString(2, entity.getOrgId());
                    ps.setString(3, entity.getUserId());
                    ps.setString(4, entity.getCreateBy());
                    ps.setTimestamp(5, Timestamp.valueOf(entity.getCreateTime()));
                    ps.setString(6, entity.getUpdateBy());
                    ps.setTimestamp(7, Timestamp.valueOf(entity.getUpdateTime()));
                    ps.setString(8, entity.getRemark());
                    ps.setBoolean(9, false);
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
    }

    private final JdbcTemplate jdbcTemplate;
}
