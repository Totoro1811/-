package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐菜品关系信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    //套餐菜品关联关系Id
    private Long id;
    //套餐Id
    private Long setmealId;
    //套餐关联菜品Id
    private Long dishId;
    //菜品名称(冗余字段)
    private String name;
    //菜品原价
    private BigDecimal price;
    //菜品份数
    private Integer copies;
}
