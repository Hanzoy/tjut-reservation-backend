package com.hanzoy.tjutreservation.pojo.po;

import lombok.Data;

/**
 * 会议查询结果实体类
 */
@Data
public class MeetingInfoPo {
    /**
     * 预约的会议id
     */
    private Integer id;

    /**
     * 会议名称
     */
    private String name;

    /**
     * 创建者，即创建者姓名
     */
    private String creator;


    /**
     * 会议日期
     */
    private String date;

    /**
     * 会议时间
     */
    private String time;


    /**
     * 会议内容
     */
    private String content;
}
