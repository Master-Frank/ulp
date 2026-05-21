/*
 * eiam-identity-source-feishu - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.identitysource.feishu.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 22:00
 */
@Data
public class BaseResponse implements Serializable {
    /**
     * 错误码，非 0 表示失败
     */
    private int    code;
    /**
     * 错误描述
     */
    private String msg;

    public boolean isSuccess() {
        return code == 0;
    }
}
