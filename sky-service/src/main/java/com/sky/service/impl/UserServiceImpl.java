package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: UserServiceImpl
 * USER: SHINIAN
 * DATE: 2023/5/21
 * DESCRIPTION : C端-用户信息服务层接口实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    //抽取微信登录的校验接口
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    //注入保存了微信小程序相关信息参数的Bean/用户持久层实现类Bean/保存了Jwt令牌相关参数的Bean
    @Resource
    private WeChatProperties weChatProperties;
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 用户登录功能
     *
     * @param userLoginDTO 用于保存用户临时身份认证码的Bean
     * @return 本次登录用户的Id / 微信的OpenId / 生成的Jwt令牌
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        //封装请求参数Map并调用提供的HttpClientUtils工具类提供发送GET请求的方法获取结果
        HashMap<String, String> paramaterMap = new HashMap<>();
        paramaterMap.put("appid", weChatProperties.getAppid()); //AppId
        paramaterMap.put("secret", weChatProperties.getSecret()); //App秘钥
        paramaterMap.put("js_code", userLoginDTO.getCode()); //临时身份验证码
        paramaterMap.put("grant_type", "authorization_code"); //授权类型(固定)
        String wxLoginResult = HttpClientUtil.doGet(WX_LOGIN_URL, paramaterMap);
        //如果是一个Json格式的字符串先将它转换为一个JSONObject的对象,再基于getString(传递键名称)
        JSONObject wxLoginJsonObject = JSON.parseObject(wxLoginResult);
        String openId = wxLoginJsonObject.getString("openid");
        //判断当前用户表中是否存在该openId对应的用户信息
        User user = userMapper.selectUserByOpenId(openId);
        if (Objects.isNull(user)) {
            log.warn("当前登录用户未注册,OpenId是 : {}", openId);
            //如果没有查询对应openId的用户信息,封装一个User插入到表中
            user = User.builder().openid(openId).createTime(LocalDateTime.now()).build();
            userMapper.insertUser(user); //▲返回主键
        }
        //生成令牌基于JwtUtil
        HashMap<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(JwtClaimsConstant.USER_ID, user.getId()); //部分(2) userId=值
        String jwtToken = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claimsMap);
        return UserLoginVO.builder().id(user.getId()).openid(user.getOpenid()).token(jwtToken).build();
    }
}
