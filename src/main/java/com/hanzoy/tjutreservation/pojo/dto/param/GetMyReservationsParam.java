package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * getMyReservations接口接受参数
 */
@Data
public class GetMyReservationsParam {
    /**
     * 用户token
     */
    @NotEmpty(message = "不能为空")
    private String token;

    /**
     * 年份
     */
    @Pattern(regexp = "^\\d{4}", message = "格式错误")
    private String year;

    /**
     * 月份
     */
    @Pattern(regexp = "^\\d{1,2}", message = "格式错误")
    private String month;
}
