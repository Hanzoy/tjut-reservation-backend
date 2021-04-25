package com.hanzoy.tjutreservation.scheduler;

import com.hanzoy.tjutreservation.scheduler.job.SendRemindJob;
import lombok.SneakyThrows;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 任务管理器
 *
 * @author meng
 */
@Service
public class QuartzManager {
    private static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);

    private Scheduler scheduler;

    public QuartzManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 添加定时任务
     * @param jobDetail
     * @param trigger
     */
    @SneakyThrows
    public void addJob(JobDetail jobDetail, Trigger trigger){

            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
    }
    /**
     * 修改某个任务的执行时间
     *
     * @param key TriggerKey
     * @param time 修改后的日期
     * @return 是否设置成功
     */
    @SneakyThrows
    public boolean modifyJob(String key, Date time) {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(key);
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(key)
                .startAt(time)
                .build();
        date = scheduler.rescheduleJob(triggerKey, trigger);
        return date != null;
    }

    /**
     * 暂停定时任务
     *
     * @param name
     * @throws SchedulerException
     * @throws Exception
     */
    @SneakyThrows
    public void pauseJob(String name){
        JobKey jobKey = new JobKey(name, name);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 删除定时任务
     *
     * @param name
     * @throws Exception
     */
    @SneakyThrows
    public void deleteJob(String name){
        JobKey jobKey = new JobKey(name, name);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        scheduler.deleteJob(jobKey);
    }

    /**
     * 重启某个定时任务
     *
     * @param name
     * @throws SchedulerException
     */
    @SneakyThrows
    public void resumeJob(String name){
        JobKey jobKey = new JobKey(name, name);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 暂停所有任务
     *
     */
    @SneakyThrows
    public void pauseAllJob(){
        scheduler.pauseAll();
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }
}

