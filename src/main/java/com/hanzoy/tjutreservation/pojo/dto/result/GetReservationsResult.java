package com.hanzoy.tjutreservation.pojo.dto.result;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

/**
 * getReservations接口返回类
 */
@Data
public class GetReservationsResult {
    /**
     * 会议室描述信息
     */
    private ArrayList<MeetingRoom> meetingRoom;

    /**
     * 日历上的会议信息
     */
    private ArrayList<Day> day;

    @Data
    public static class MeetingRoom{
        /**
         * 会议室id
         */
        private Integer roomid;

        /**
         * 会议室名字
         */
        private String name;

        /**
         * 会议室开放日期
         */
        private String date;

        /**
         * 会议室开发时间
         */
        private String time;

        /**
         * 会议室备注
         */
        private String remark;
    }

    @Data
    public static class Day{
        /**
         * 日期
         */
        private Integer dayOfMonth;

        /**
         * 当前日期下的总会议个数
         */
        private Integer count;

        /**
         * 某日期下会议的预约信息
         */
        private ArrayList<MeetingRoomInfo> meetingRoomInfo;

        @Data
        public static class MeetingRoomInfo{
            /**
             * 会议室id
             */
            private Integer roomid;

            /**
             * 对应日期下该会议室预约人数
             */
            private Integer count;

            /**
             * 会议室预约进度条
             */
            private ArrayList<Bar> bar;

            /**
             * 对应日期下的对应会议室下的预约信息概览
             */
            private ArrayList<MeetingInfo> meetingInfo;

            @Data
            public static class Bar{
                /**
                 * 会议进度条开始位置
                 */
                private Double start;

                /**
                 * 会议室结束位置
                 */
                private Double end;
            }
            @Data
            public static class MeetingInfo{
                /**
                 * 会议id
                 */
                private Integer id;

                /**
                 * 会议名称
                 */
                private String name;

                /**
                 * 会议创建者
                 */
                private String creator;

                /**
                 * 申请日期
                 */
                private String date;

                /**
                 * 申请时间
                 */
                private String time;
            }
        }
    }
}
