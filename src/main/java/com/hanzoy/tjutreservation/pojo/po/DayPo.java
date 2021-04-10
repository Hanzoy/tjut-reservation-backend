package com.hanzoy.tjutreservation.pojo.po;

import lombok.Data;

/**
 * selectRiLiByYearAndMonth 查找数据库的返回结果映射类
 */
@Data
public class DayPo {
    /**
     * 日期
     */
    private String dayOfMonth;

    /**
     * 当前日期下的总会议个数
     */
    private Integer count;
}
