package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户Id
    private Long id;
    //微信用户唯一标识(用于判断用户是否注册)
    private String openid;
    //用户姓名
    private String name;
    //用户手机号
    private String phone;
    //用户性别 0:女 1:男
    private String sex;
    //用户身份证号
    private String idNumber;
    //用户头像
    private String avatar;
    //用户注册时间
    private LocalDateTime createTime;
}
