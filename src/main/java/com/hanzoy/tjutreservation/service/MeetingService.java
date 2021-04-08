package com.hanzoy.tjutreservation.service;

import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.PostReservationParam;

public interface MeetingService {
    /**
     * 用户创建会议
     * @param param 参数
     * @return 返回对象
     */
    CommonResult postReservation(PostReservationParam param);
}
