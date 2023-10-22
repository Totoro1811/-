package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishFlavorMapper
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : B端-菜品口味信息持久层接口
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量添加菜品口味信息
     *
     * @param flavorList 菜品口味信息集合
     */
    void batchInsertDishFlavor(@Param("flavorList") List<DishFlavor> flavorList);

    /**
     * 基于菜品Id批量删除菜品口味数据
     *
     * @param ids 要删除的菜品Id
     */
    void batchDeleteDishFlavor(@Param("ids") List<Long> ids);

    /**
     * 基于菜品Id查询菜品口味
     *
     * @param dishId 菜品Id
     * @return 菜品Id对应的所有口味信息List集合
     */
    List<DishFlavor> selectDishFlavorByDishId(@Param("dishId") Long dishId);
}
