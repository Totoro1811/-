package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 员工添加数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO implements Serializable {
    //员工Id
    private Long id;
    //登录用户名
    private String username;
    //真实姓名
    private String name;
    //手机号码
    private String phone;
    //员工性别
    private String sex;
    //身份证号
    private String idNumber;
}
