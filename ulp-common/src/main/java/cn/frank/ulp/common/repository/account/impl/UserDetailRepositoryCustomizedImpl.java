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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.account.UserDetailEntity;
import cn.frank.ulp.common.repository.account.UserDetailRepositoryCustomized;

/**
 * User Detail Repository Customized
 *
 * @author Frank Zhang
 */
@Repository
public class UserDetailRepositoryCustomizedImpl implements UserDetailRepositoryCustomized {

    /**
     * 批量更新或新增
     *
     * @param data {@link List}
     */
    @Override
    public void batchSave(List<UserDetailEntity> data) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO ulp_user_detail (id_, user_id, id_type, id_card, website_,address_,create_by,create_time,update_by,update_time,remark_,is_deleted) values (?,?,?,?,?,?,?,?,?,?,?,?)",
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                    UserDetailEntity entity = data.get(i);
                    ps.setString(1, entity.getId());
                    ps.setString(2, entity.getUserId());
                    ps.setString(3,
                        Objects.isNull(entity.getIdType()) ? null : entity.getIdType().getCode());
                    ps.setString(4, entity.getIdCard());
                    ps.setString(5, entity.getWebsite());
                    ps.setString(6, entity.getAddress());
                    ps.setString(7, entity.getCreateBy());
                    ps.setTimestamp(8, Timestamp.valueOf(entity.getCreateTime()));
                    ps.setString(9, entity.getUpdateBy());
                    ps.setTimestamp(10, Timestamp.valueOf(entity.getUpdateTime()));
                    ps.setString(11, entity.getRemark());
                    ps.setBoolean(12, false);
                }

                @Override
                public int getBatchSize() {
                    return data.size();
                }
            });
    }

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    @Override
    public void batchUpdate(ArrayList<UserDetailEntity> list) {
        jdbcTemplate.batchUpdate(
            "UPDATE  ulp_user_detail SET user_id=?,id_type=?, id_card=?, website_=? ,address_=?,create_by=?,create_time=?,update_by=?,update_time=?,remark_=? WHERE id_=?",
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                    UserDetailEntity entity = list.get(i);
                    ps.setString(1, entity.getUserId());
                    ps.setString(2,
                        Objects.isNull(entity.getIdType()) ? null : entity.getIdType().getCode());
                    ps.setString(3, entity.getIdCard());
                    ps.setString(4, entity.getWebsite());
                    ps.setString(5, entity.getAddress());
                    ps.setString(6, entity.getCreateBy());
                    ps.setTimestamp(7, Timestamp.valueOf(entity.getCreateTime()));
                    ps.setString(8, entity.getUpdateBy());
                    ps.setTimestamp(9, Timestamp.valueOf(entity.getUpdateTime()));
                    ps.setString(10, entity.getRemark());
                    ps.setString(11, entity.getId());
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
    }

    private final JdbcTemplate jdbcTemplate;

    public UserDetailRepositoryCustomizedImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
