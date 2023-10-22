package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 购物车数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO implements Serializable {
    //菜品Id
    private Long dishId;
    //套餐Id
    private Long setmealId;
    //菜品口味
    private String dishFlavor;
}
