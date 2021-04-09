package com.hanzoy.tjutreservation.pojo.dto.result;

import com.hanzoy.utils.ClassCopyUtils.CopyProperty;
import lombok.Data;

import java.util.ArrayList;

/**
 * getMyReservations接口返回类
 */
@Data
public class GetMyReservationsResult {
    /**
     * 当前年下的每月预约量集合
     */
    private ArrayList<Integer> months;

    /**
     * 当前月下会议预约集合
     */
    private ArrayList<Meeting> info;

    @Data
    public static class Meeting{
        /**
         * 会议id
         */
        private Integer id;

        /**
         * 会议名称
         */
        private String name;

        /**
         * 创建者
         */
        private String creator;

        /**
         * 会议室名
         */
        @CopyProperty("roomId")
        private String meetingName;

        /**
         * 会议预约日期
         */
        private String date;

        /**
         * 会议预约时间
         */
        private String time;

        /**
         * 会议预约状态
         */
        private String status;
    }
}
