package com.sky.vo;

import com.sky.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜品详细信息(响应数据)存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishVO implements Serializable {
    //菜品Id
    private Long id;
    //菜品名称
    private String name;
    //菜品分类Id
    private Long categoryId;
    //菜品价格
    private BigDecimal price;
    //菜品图片
    private String image;
    //菜品描述信息
    private String description;
    //菜品状态 0:停售 1:起售
    private Integer status;
    //菜品更新时间
    private LocalDateTime updateTime;
    //菜品分类名称
    private String categoryName;
    //菜品关联的口味
    private List<DishFlavor> flavors = new ArrayList<>();
}
