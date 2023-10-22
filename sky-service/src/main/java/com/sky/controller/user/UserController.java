package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: UserController
 * USER: SHINIAN
 * DATE: 2023/5/21
 * DESCRIPTION : C端-用户信息的表现层
 */
@Slf4j
@Api(tags = "C端-用户信息的表现层")
@RequestMapping("/user/user")
@RestController
public class UserController {

    //注入用户服务层接口的实现类Bean
    @Resource
    private UserService userService;

    /**
     * 用户登录功能
     *
     * @param userLoginDTO 用于保存用户临时身份认证码的Bean
     * @return 全局通用返回信息Bean(本次登录用户的Id / 微信的OpenId / 生成的Jwt令牌)
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("本次用户登录的临时身份验证码是 : {}", userLoginDTO);
        //调用服务层传递userLoginDTO接收用户信息VO
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }
}
