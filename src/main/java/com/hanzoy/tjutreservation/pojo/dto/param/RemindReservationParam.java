package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * remindReservation接口参数
 */
@Data
public class RemindReservationParam {
    /**
     * 用户token
     */
    @NotEmpty(message = "不能为空")
    private String token;

    /**
     * 会议id
     */
    @Pattern(regexp = "^[0-9]*$", message = "格式错误")
    private Integer id;

    /**
     * 是否开启提醒
     */
    @NotNull(message = "不能为空")
    private Boolean remind;
}
