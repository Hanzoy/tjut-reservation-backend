package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class DeleteReservationParam {
    /**
     * 用户token
     */
    @NotEmpty(message = "不能为空")
    private String token;

    /**
     * 会议id
     */
    @Pattern(regexp = "^[0-9]*$", message = "格式错误")
    private String id;

    /**
     * 删除备注
     */
    private String remark;
}
