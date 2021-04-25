package com.hanzoy.tjutreservation.service.impl;

import com.hanzoy.tjutreservation.scheduler.QuartzManager;
import com.hanzoy.tjutreservation.scheduler.job.SendRemindJob;
import com.hanzoy.tjutreservation.service.SchedulerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    QuartzManager quartzScheduler;

    @Autowired
    QuartzManager quartzManager;

    @Override
    public void starSendRemindTask(String meetingId, Date startTime) {
        //需要提前通知的时间
        java.util.Calendar calendar = new GregorianCalendar();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE,-15); //把日期往后增加一天,整数  往后推,负数往前移动
        Date limitDate=calendar.getTime();
        Date now = new Date();
        if(now.after(limitDate)){
            startTime = now;
        }else{
            startTime = limitDate;
        }

        // 创建jobDetail实例，绑定Job实现类,指明job的名称，所在组的名称，以及绑定job类
        JobDetail jobDetail = JobBuilder
                .newJob(SendRemindJob.class)
                .withIdentity(meetingId)
                .build();
        // 定时任务参数
        jobDetail.getJobDataMap().put("meetingId", meetingId);
        // 触发器
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(meetingId)
                .startAt(startTime)
                .build();

        // 调度容器设置JobDetail和Trigger
        quartzManager.addJob(jobDetail, trigger);
//            quartzScheduler.scheduleJob(jobDetail, trigger);
    }
}
