package com.hanzoy.tjutreservation.service;

import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.LoginAuthParam;
import com.hanzoy.tjutreservation.pojo.dto.param.LoginParam;

public interface UserService {
    /**
     * 用户通过code登陆
     * @param param 参数
     * @return 返回对象
     */
    CommonResult login(LoginParam param);

    /**
     * 第一次登陆授权并完善信息
     * @param param 参数
     * @return 返回对象
     */
    CommonResult loginAuth(LoginAuthParam param);
}
