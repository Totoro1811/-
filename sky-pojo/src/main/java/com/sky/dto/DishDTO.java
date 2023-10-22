package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜品添加数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO implements Serializable {
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
    //菜品描述信息
    private String description;
    //菜品状态 0:停售 1:起售
    private Integer status;
    //菜品口味List集合
    private List<DishFlavor> flavors = new ArrayList<>();

}
