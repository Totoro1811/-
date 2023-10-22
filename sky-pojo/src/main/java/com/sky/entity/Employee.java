package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    //员工Id
    private Long id;
    //员工登录名称
    private String username;
    //员工真实姓名
    private String name;
    //员工登录密码
    private String password;
    //员工手机号码
    private String phone;
    //员工性别
    private String sex;
    //员工身份证号
    private String idNumber;
    //员工状态(0:禁用 1:启用)
    private Integer status;
    //员工信息创建时间
    private LocalDateTime createTime;
    //员工信息更新时间
    private LocalDateTime updateTime;
    //员工信息创建人Id
    private Long createUser;
    //员工信息更新人Id
    private Long updateUser;
}
