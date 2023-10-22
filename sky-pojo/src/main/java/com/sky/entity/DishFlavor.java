package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {
    private static final long serialVersionUID = 1L;
    //菜品口味Id
    private Long id;
    //菜品口味关联菜品Id
    private Long dishId;
    //口味名称
    private String name;
    //口味数据JSON数据
    private String value;
}
