package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;
    //购物车Id
    private Long id;
    //购物中的菜品/套餐名称
    private String name;
    //购物车关联用户Id
    private Long userId;
    //购物车信息的菜品Id
    private Long dishId;
    //购物车信息的套餐Id
    private Long setmealId;
    //购物车信息的口味
    private String dishFlavor;
    //购物车信息的菜品/套餐数量
    private Integer number;
    //购物车信息的菜品/套餐金额
    private BigDecimal amount;
    //图片
    private String image;
    //购物车信息的创建时间
    private LocalDateTime createTime;
}
