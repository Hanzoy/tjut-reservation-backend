package com.hanzoy.tjutreservation.service.impl;

import com.hanzoy.tjutreservation.mapper.UserMapper;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.LoginAuthParam;
import com.hanzoy.tjutreservation.pojo.dto.param.LoginParam;
import com.hanzoy.tjutreservation.pojo.dto.result.LoginAuthResult;
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
import java.util.ArrayList;

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

    @Override
    public CommonResult loginAuth(LoginAuthParam param) {
        //调取微信后端登陆接口
        AuthorizationResult authorizationResult = wechatUtils.js_codeToSession(param.getCode());

        //创建返回体实体对象
        LoginAuthResult loginAuthResult = new LoginAuthResult();

        //判断是否通过微信接口拿到数据且没有报错
        if (authorizationResult != null && authorizationResult.getErrcode() == null){
            //通过name搜索数据库中是否有预设的user
            ArrayList<UserPo> users = userMapper.selectUserByName(param.getName());

            //是否存在预设标志
            Boolean havePresupposition = false;
            //遍历users并查看是否有avatar_url为空的user
            for (UserPo user : users) {
                //当查找到存在有user，其avatar_url为空的时，完善该user
                if(user.getAvatarUrl() == null || user.getAvatarUrl().equals("")){
                    //将标志设为false
                    havePresupposition = true;
                    //预留openId以便后续数据库更新操作
                    String openid = user.getOpenid();
                    //完善信息
                    user.setAvatarUrl(param.getAvatarUrl());
                    user.setNickName(param.getNickName());
                    user.setOpenid(authorizationResult.getOpenid());
                    //保存至数据库
                    userMapper.updateUserByOpneidWithOpenid(
                            openid,
                            authorizationResult.getOpenid(),
                            param.getName(),
                            param.getNickName(),
                            param.getAvatarUrl(),
                            true);
                    //将openid与name写入token中
                    String token = jwtUtils.createTokenCustomFields(user, "openid", "name");
                    //设置token
                    loginAuthResult.setToken(token);
                    break;
                }
            }
            //如果没有找到预设
            if(!havePresupposition){
                //检查用户是否存在
                UserPo user = userMapper.selectUserByOpenid(authorizationResult.getOpenid());
                if(user != null){
                    //如果存在用户

                    //则修改数据库中内容
                    userMapper.updateUserByOpneidWithOpenid(
                            authorizationResult.getOpenid(),
                            authorizationResult.getOpenid(),
                            param.getName(),
                            param.getNickName(),
                            param.getAvatarUrl(),
                            user.getCreatAuth());
                }else{
                    //如果不存在用户
                    //将前端传入的数据插入到数据库中，并且将creatAuth设置为false
                    userMapper.insertUser(
                            authorizationResult.getOpenid(),
                            param.getName(),
                            param.getNickName(),
                            param.getAvatarUrl(),
                            false);

                    //制作一份user实体数据类
                    user = new UserPo();
                    user.setName(param.getName());
                    user.setOpenid(authorizationResult.getOpenid());
                }

                //将openid与name写入token中
                String token = jwtUtils.createTokenCustomFields(user, "openid", "name");
                //设置token
                loginAuthResult.setToken(token);
            }
        }else{
            //如果没有拿到微信数据则返回异常
            assert authorizationResult != null;
            return CommonResult.fail(ResultEnum.WECHAT_SERVER_ERROR.getCode(), authorizationResult.getErrmsg());
        }
        return CommonResult.success(loginAuthResult);
    }
}
