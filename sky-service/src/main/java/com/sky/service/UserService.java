package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: UserService
 * USER: SHINIAN
 * DATE: 2023/5/21
 * DESCRIPTION : C端-用户信息服务层接口
 */
public interface UserService {
    /**
     * 用户登录功能
     *
     * @param userLoginDTO 用于保存用户临时身份认证码的Bean
     * @return 本次登录用户的Id / 微信的OpenId / 生成的Jwt令牌
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
