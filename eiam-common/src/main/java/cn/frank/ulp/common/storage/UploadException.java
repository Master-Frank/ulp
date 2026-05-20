/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.storage;

import java.io.Serial;

import org.springframework.http.HttpStatus;

/**
 * 存储服务异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/19 22:53
 */
public class UploadException extends StorageProviderException {
    @Serial
    private static final long serialVersionUID = 6249098979022610064L;

    public UploadException(Throwable cause) {
        super(cause, "Upload fail", cause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
