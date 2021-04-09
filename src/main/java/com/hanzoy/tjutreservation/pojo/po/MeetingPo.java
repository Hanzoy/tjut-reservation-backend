package com.hanzoy.tjutreservation.pojo.po;

import lombok.Data;

/**
 * 会议查询结果实体类
 */
@Data
public class MeetingPo {
    /**
     * 预约的会议id
     */
    private Integer id;

    /**
     * 会议名称
     */
    private String name;

    /**
     * 创建者，即创建者openid
     */
    private String creator;

    /**
     * 会议室id
     */
    private String roomId;

    /**
     * 会议日期
     */
    private String date;

    /**
     * 会议开始时间
     */
    private String startTime;

    /**
     * 会议结束时间
     */
    private String endTime;

    /**
     * 会议内容
     */
    private String content;
}
