package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

/**
 * joinReservation接口参数
 */
@Data
public class JoinReservationParam {
    /**
     * 用户token
     */
    private String token;

    /**
     * 会议id
     */
    private Integer id;

    /**
     * 加入或者退出
     */
    private Boolean join;
}
