package com.hanzoy.tjutreservation.pojo.dto.result;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

/**
 * getReservation接口返回参数
 */
@Data
public class GetReservationResult {
    /**
     * 是否为创建者
     */
    private Boolean isCreator;

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
     * 会议室姓名
     */
    private String meetingName;

    /**
     * 日期
     */
    private String date;

    /**
     * 时间
     */
    private String time;

    /**
     * 会议内容
     */
    private String content;

    /**
     * 会议状态
     */
    private String status;

    /**
     * 是否开启会议提醒
     */
    private Boolean remind;

    /**
     * 参会人员
     */
    private ArrayList<User> participant;

    @Data
    public static class User{
        /**
         * 参会人员openid
         */
        private String id;

        /**
         * 参会人姓名
         */
        private String name;

        /**
         * 参会人微信名称
         */
        private String nickName;

        /**
         * 参会人微信头像
         */
        private String avatarUrl;
    }
}
