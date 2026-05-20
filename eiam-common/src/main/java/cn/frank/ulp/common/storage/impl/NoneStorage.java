/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.storage.impl;

import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import cn.frank.ulp.common.storage.Storage;
import cn.frank.ulp.common.storage.StorageProviderException;

/**
 * 本地存储配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:32
 */
public class NoneStorage implements Storage {

    public NoneStorage() {
    }

    @Override
    public String upload(@NotNull String fileName,
                         InputStream inputStream) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }

    @Override
    public String download(String path) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }
}
