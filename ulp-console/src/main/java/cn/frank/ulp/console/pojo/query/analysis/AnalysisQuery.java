/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.pojo.query.analysis;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 统计查询入惨
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "统计查询入惨")
public class AnalysisQuery implements Serializable {

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime startTime;

    /**
     * 截止日期
     */
    @NotNull(message = "截止日期不能为空")
    @Schema(description = "截止日期")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime endTime;

    public Interval getTimeInterval() {
        Period interval = Period.between(startTime.toLocalDate(), endTime.toLocalDate());
        boolean today = interval.getDays() < 1;
        boolean isDay = interval.getMonths() < 1;
        return today ? Interval.HOUR : isDay ? Interval.DAY : Interval.MONTH;
    }

    @Getter
    public enum Interval {
                          /**
                           * HOUR
                           */
                          HOUR("hour", "%H时"),
                          /**
                           * DAY
                           */
                          DAY("day", "%d日"),
                          /**
                           * MONTH
                           */
                          MONTH("month", "%m月");

        private final String type;
        private final String format;

        Interval(String type, String format) {
            this.type = type;
            this.format = format;
        }
    }
}
