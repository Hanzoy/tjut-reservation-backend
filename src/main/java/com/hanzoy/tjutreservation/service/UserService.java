package com.hanzoy.tjutreservation.service;

import com.hanzoy.tjutreservation.pojo.bo.UserTokenInfo;
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

    /**
     * 业务封装方法，将User内容写入token
     * @param user 需要解析的实体类
     * @return token
     */
    String writeUserToToken(Object user);

    /**
     * 业务封装方法，解析token内容并返回一个UserTokenInfo的实体类
     * @param token 需要解析的token
     * @return 带有token信息的实体类
     */
    UserTokenInfo getUserTokenInfo(String token);
}
