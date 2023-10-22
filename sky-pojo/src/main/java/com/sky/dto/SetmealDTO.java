package com.sky.dto;

import com.sky.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 套餐信息添加数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetmealDTO implements Serializable {
    //套餐Id
    private Long id;
    //分类Id
    private Long categoryId;
    //套餐名称
    private String name;
    //套餐价格
    private BigDecimal price;
    //状态 0:停用 1:启用
    private Integer status;
    //描述信息
    private String description;
    //图片
    private String image;
    //套餐菜品关系
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
