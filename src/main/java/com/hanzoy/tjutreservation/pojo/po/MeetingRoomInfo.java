package com.hanzoy.tjutreservation.pojo.po;

import lombok.Data;

/**
 * selectAllRoomInfoByDate 查找数据库的返回结果映射类
 */
@Data
public class MeetingRoomInfo {
    /**
     * 会议室id
     */
    private Integer roomid;

    /**
     * 当前会议室预定的会议数量
     */
    private Integer count;
}
