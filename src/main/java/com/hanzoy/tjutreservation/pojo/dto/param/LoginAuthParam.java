package com.hanzoy.tjutreservation.pojo.dto.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * loginAuth接口接受参数类
 */
@Data
public class LoginAuthParam {
    /**
     * 用户登陆调用微信登陆接口的code
     */
    @NotEmpty(message = "不能为空")
    private String code;

    /**
     * 用户自己输入的name
     */
    @NotEmpty(message = "不能为空")
    private String name;

    /**
     * 微信昵称
     */
    @NotEmpty(message = "不能为空")
    private String nickName;

    /**
     * 微信头像
     */
    @NotEmpty(message = "不能为空")
    private String avatarUrl;
}
