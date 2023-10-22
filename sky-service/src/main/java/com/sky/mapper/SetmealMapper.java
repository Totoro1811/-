package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealMapper
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : B端-套餐信息持久层接口
 */
@Mapper
public interface SetmealMapper {

    /**
     * 基于分类Id聚合查询套餐数量
     *
     * @param categoryId 分类Id
     * @return 分类Id对应的套餐数量
     */
    Integer selectSetmealCountByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 基于菜品Id查询套餐数量
     *
     * @param dishId 菜品对应Id
     * @return 菜品Id对应的套餐数量
     */
    Integer selectSetmealCountByDishId(@Param("dishId") Long dishId);

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO 套餐分页查询信息封装Bean
     * @return 分页查询结果封装Bean
     */
    Page<SetmealVO> selectByPage(@Param("setmealPageQueryDTO") SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 添加套餐基本信息
     *
     * @param setmeal 套餐基本信息
     */
    @AutoFill(OperationType.INSERT)
    void insertSetmeal(@Param("setmeal") Setmeal setmeal);

    /**
     * 批量添加套餐中的菜品信息
     *
     * @param setmealDishList 套餐菜品信息List集合
     */
    void batchInsertSetmealDish(@Param("setmealDishList") List<SetmealDish> setmealDishList);

    /**
     * 基于套餐Id查询套餐状态
     *
     * @param setmealId 套餐Id
     * @return 套餐状态
     */
    Integer selectStatusById(@Param("setmealId") Long setmealId);

    /**
     * 基于套餐Id删除套餐基本信息
     *
     * @param deleteSetmealId 套餐Id
     */
    void deleteById(@Param("deleteSetmealId") Long deleteSetmealId);

    /**
     * 基于套餐Id删除套餐信息表中的关联信息
     *
     * @param deleteSetmealId 套餐Id
     */
    void deleteSetmealDishBySetmealId(@Param("deleteSetmealId") Long deleteSetmealId);

    /**
     * 基于套餐Id查询套餐基本信息
     *
     * @param setmealId 套餐Id
     * @return 套餐基本信息
     */
    Setmeal selectById(@Param("setmealId") Long setmealId);

    /**
     * 基于套餐Id查询套餐菜品表关联信息
     *
     * @param setmealId 套餐Id
     * @return 套餐菜品信息List集合
     */
    List<SetmealDish> selectSetmealDishBySetmealId(@Param("setmealId") Long setmealId);

    /**
     * 基于套餐Id更新套餐信息
     *
     * @param newSetmeal 更新后的套餐信息(包含Id)
     */
    @AutoFill(OperationType.UPDATE)
    void updateSetmeal(@Param("setmeal") Setmeal newSetmeal);

    /**
     * 根据分类Id查询套餐列表信息功能
     *
     * @param conditionSetmeal 套餐查询信息
     * @return 套餐列表
     */
    List<Setmeal> selectByCondition(@Param("conditionSetmeal") Setmeal conditionSetmeal);

    /**
     * 基于套餐状态查询套餐数量
     *
     * @param status 套餐状态
     * @return 指定状态的套餐数量
     */
    Integer selectSetmealCOuntByStatus(@Param("status") Integer status);
}
