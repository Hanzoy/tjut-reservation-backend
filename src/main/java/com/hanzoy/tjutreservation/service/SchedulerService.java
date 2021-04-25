package com.hanzoy.tjutreservation.service;

import java.util.Date;

public interface SchedulerService {
    public void starSendRemindTask(String meetingId, Date startTime);
}
