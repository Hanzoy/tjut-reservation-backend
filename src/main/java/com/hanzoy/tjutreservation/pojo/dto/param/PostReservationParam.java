package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * postReservation接口接受参数
 */
@Data
public class PostReservationParam {
    /**
     * 用户token
     */
    @NotEmpty(message = "不能为空")
    private String token;

    /**
     * 会议名称
     */
    @NotEmpty(message = "不能为空")
    private String name;

    /**
     * 会议室id
     */
    @Pattern(regexp = "^[0-9]*$", message = "必须为数字")
    private String roomId;

    /**
     * 预定日期
     */
    @NotEmpty(message = "不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}", message = "格式错误")
    private String date;

    /**
     * 预定时间
     */
    @NotEmpty(message = "不能为空")
    @Pattern(regexp = "^\\d{2}:\\d{2}", message = "格式错误")
    private String startTime;

    /**
     * 预定时间
     */
    @NotEmpty(message = "不能为空")
    @Pattern(regexp = "^\\d{2}:\\d{2}", message = "格式错误")
    private String endTime;

    /**
     * 会议内容
     */
    @NotEmpty(message = "不能为空")
    private String content;
}
