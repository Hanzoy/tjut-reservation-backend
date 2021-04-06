package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * login接口的接受参数类
 */
@Data
public class LoginParam {
    /**
     * 前端通过微信登陆接口获取的code
     */
    @NotEmpty(message = "不能为空")
    private String code;
}
