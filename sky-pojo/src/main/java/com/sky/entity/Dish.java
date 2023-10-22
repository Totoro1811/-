package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {
    private static final long serialVersionUID = 1L;
    //菜品Id
    private Long id;
    //菜品名称
    private String name;
    //菜品分类id
    private Long categoryId;
    //菜品价格
    private BigDecimal price;
    //菜品图片
    private String image;
    //描述信息
    private String description;
    //菜品状态 0:停售 1:起售
    private Integer status;
    //菜品创建时间
    private LocalDateTime createTime;
    //菜品更新时间
    private LocalDateTime updateTime;
    //菜品创建人Id
    private Long createUser;
    //菜品更新人Id
    private Long updateUser;
}
