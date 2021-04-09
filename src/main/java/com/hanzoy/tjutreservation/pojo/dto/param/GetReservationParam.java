package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * getReservation接口接受参数
 */
@Data
public class GetReservationParam {
    /**
     * 用户token
     */
    @NotEmpty(message = "不能为空")
    private String token;

    /**
     * 会议室id
     */
    @Pattern(regexp = "^[0-9]*$", message = "格式错误")
    private String id;
}
