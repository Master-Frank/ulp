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
package cn.frank.ulp.common.constant;

import cn.frank.ulp.support.constant.EiamConstants;

/**
 * 同步器常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/20 22:31
 */
public final class SynchronizerConstants {

    /**
     * 同步器路径
     */
    public final static String SYNCHRONIZER_PATH  = EiamConstants.V1_API_PATH + "/synchronizer";

    /**
     * 同步器事件接收路径
     */
    public final static String EVENT_RECEIVE_PATH = SYNCHRONIZER_PATH + "/event_receive";
}
