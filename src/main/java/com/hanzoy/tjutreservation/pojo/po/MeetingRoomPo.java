package com.hanzoy.tjutreservation.pojo.po;

import lombok.Data;

//会议室查询结果实体类
@Data
public class MeetingRoomPo {
    /**
     * 会议室主键，唯一标识
     */
    private Integer id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 会议室开放日期
     */
    private String date;

    /**
     * 会议室开放时间
     */
    private String time;

    /**
     * 备注
     */
    private String remark;
}
