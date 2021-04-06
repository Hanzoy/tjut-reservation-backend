package com.hanzoy.tjutreservation.service.impl;

import com.hanzoy.tjutreservation.mapper.UserMapper;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.LoginParam;
import com.hanzoy.tjutreservation.pojo.dto.result.LoginResult;
import com.hanzoy.tjutreservation.pojo.dto.resultEnum.ResultEnum;
import com.hanzoy.tjutreservation.pojo.po.UserPo;
import com.hanzoy.tjutreservation.service.UserService;
import com.hanzoy.tjutreservation.utils.WechatUtils.WechatUtils;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.AuthorizationResult;
import com.hanzoy.utils.JWTUtils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    WechatUtils wechatUtils;

    @Resource
    UserMapper userMapper;

    @Autowired
    JWTUtils jwtUtils;

    @Override
    public CommonResult login(LoginParam param) {
        //调取微信后端登陆接口
        AuthorizationResult authorizationResult = wechatUtils.js_codeToSession(param.getCode());

        //创建返回实体类的对象
        LoginResult loginResult = new LoginResult();

        //判断是否通过微信接口拿到数据且没有报错
        if (authorizationResult != null && authorizationResult.getErrcode() == null){
            //获取openid
            String openid = authorizationResult.getOpenid();
            //通过openid查找对应的用户
            UserPo user = userMapper.selectUserByOpenid(openid);
            if (user == null){
                //没有找到用户说明还未进行权限认证

                //设置需要权限
                loginResult.setNeedAuth(true);

                //设置token为null
                loginResult.setToken(null);
            }else{
                //找到用户说明已完成权限认证

                //设置不需要权限认证
                loginResult.setNeedAuth(false);

                //将openid与name写入token中
                String token = jwtUtils.createTokenCustomFields(user, "openid", "name");
                //设置token
                loginResult.setToken(token);
            }
        }else{
            //如果没有拿到微信数据则返回异常
            assert authorizationResult != null;
            return CommonResult.fail(ResultEnum.WECHAT_SERVER_ERROR.getCode(), authorizationResult.getErrmsg());
        }
        //将返回对象写入data并返回
        return CommonResult.success(loginResult);
    }
}
