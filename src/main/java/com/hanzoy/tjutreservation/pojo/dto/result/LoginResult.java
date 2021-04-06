package com.hanzoy.tjutreservation.pojo.dto.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * login接口的返回类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResult {
    /**
     * 前端是否还需要一次权限验证
     */
    private Boolean needAuth;

    /**
     * 用户token
     */
    private String token;
}
