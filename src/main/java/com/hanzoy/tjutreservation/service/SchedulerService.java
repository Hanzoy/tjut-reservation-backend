package com.hanzoy.tjutreservation.service;

import java.util.Date;

public interface SchedulerService {
    /**
     * 开启发送提醒任务
     * @param meetingId 会议id
     * @param startTime 会议开始时间
     */
    void startSendRemindTask(String meetingId, Date startTime);

    /**
     * 修改发送提醒任务
     * @param meetingId 会议id
     * @param startTime 会议开始时间
     */
    void modifySendRemindTask(String meetingId, Date startTime);
}
