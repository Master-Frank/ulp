/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.service.account;

import java.io.Serializable;
import java.util.List;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.account.query.UserListNotInGroupQuery;
import cn.frank.ulp.common.entity.account.query.UserListQuery;
import cn.frank.ulp.common.enums.CheckValidityType;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.console.pojo.result.account.BatchUserResult;
import cn.frank.ulp.console.pojo.result.account.UserListResult;
import cn.frank.ulp.console.pojo.result.account.UserLoginAuditListResult;
import cn.frank.ulp.console.pojo.result.account.UserResult;
import cn.frank.ulp.console.pojo.save.account.UserCreateParam;
import cn.frank.ulp.console.pojo.update.account.ResetPasswordParam;
import cn.frank.ulp.console.pojo.update.account.UserUpdateParam;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-07-31
 */
public interface UserService {

    /**
     * 获取用户（分页）
     *
     * @param page  {@link PageModel}
     * @param query {@link UserListQuery}
     * @return {@link UserListQuery}
     */
    Page<UserListResult> getUserList(PageModel page, UserListQuery query);

    /**
     * 获取用户列表不在当前组
     *
     * @param page  {@link  PageModel}
     * @param query {@link UserListNotInGroupQuery }
     * @return {@link  }
     */
    Page<UserListResult> getUserListNotInGroup(PageModel page, UserListNotInGroupQuery query);

    /**
     * 重置密码
     *
     * @param param       {@link ResetPasswordParam} 用户入参
     * @return {@link Boolean} 是否成功
     */
    Boolean resetUserPassword(ResetPasswordParam param);

    /**
     * 更改用户状态
     *
     * @param id     {@link Long}
     * @param status {@link UserStatus}
     * @return {@link Boolean}
     */
    boolean changeUserStatus(String id, UserStatus status);

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    Boolean createUser(UserCreateParam param);

    /**
     * 通过外部ID获取用户
     *
     * @param id {@link String}
     * @return {@link UserEntity}
     */
    UserEntity getByExternalId(String id);

    /**
     * 根据ID查询用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    UserResult getUser(String id);

    /**
     * 更新用户
     *
     * @param param {@link UserUpdateParam}
     * @return {@link Boolean}
     */
    boolean updateUser(UserUpdateParam param);

    /**
     * 删除用户
     *
     * @param id {@link Serializable}
     * @return {@link Boolean}
     */
    boolean deleteUser(String id);

    /**
     * 批量删除用户
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    Boolean batchDeleteUser(String[] ids);

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean}
     */
    Boolean userParamCheck(CheckValidityType type, String value, String id);

    /**
     * 查看用户登录日志
     *
     * @param id {@link Long}
     * @param pageModel {@link PageModel}
     * @return {@link   List}
     */
    Page<UserLoginAuditListResult> findUserLoginAuditList(String id, PageModel pageModel);

    /**
     * 批量获取用户信息
     *
     * @param ids {@link List}
     * @return {@link List}
     */
    List<BatchUserResult> batchGetUser(List<String> ids);
}
