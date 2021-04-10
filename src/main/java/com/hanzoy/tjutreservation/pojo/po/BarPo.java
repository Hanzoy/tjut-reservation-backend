package com.hanzoy.tjutreservation.pojo.po;

import lombok.Data;

/**
 * selectBarByDateAndRoomId 查找数据库的返回结果映射类
 */
@Data
public class BarPo {
    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


}
