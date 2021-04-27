package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * modifyReservation接口参数
 */
@Data
public class ModifyReservationParam {
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
     * 会议id
     */
    @NotNull(message = "不能为空")
    private Integer id;

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

    /**
     * 是否提醒
     */
    private Boolean remind;
}
