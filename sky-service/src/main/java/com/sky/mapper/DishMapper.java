package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishMapper
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : B端-菜品信息持久层接口
 */
@Mapper
public interface DishMapper {

    /**
     * 基于分类Id查询菜品数量
     *
     * @param categoryId 菜品分类Id
     * @return 分类Id对应的菜品数量
     */
    Integer selectDishCountByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 添加菜品数据
     *
     * @param dish 菜品基本信息
     */
    @AutoFill(OperationType.INSERT)
    void insertDish(@Param("dish") Dish dish);

    /**
     * 分页条件查询菜品数据
     *
     * @param dishPageQueryDTO 分页查询的条件(页码/分页条数/菜品名称/分类Id/菜品状态)
     * @return DishVO(包含了Dish的基本信息 + Dish对应的分类名称)的Bean ▲让MyBatis自动根据查询的结果完成数据的封装
     */
    Page<DishVO> selectDishByPage(@Param("dishPageQueryDTO") DishPageQueryDTO dishPageQueryDTO);

    /**
     * 基于菜品Id查询菜品信息
     *
     * @param dishId 菜品Id
     * @return 菜品信息
     */
    Dish selectDishById(@Param("dishId") Long dishId);

    /**
     * 批量删除菜品数据
     *
     * @param ids 要删除的菜品Id集合
     */
    void batchDeleteDish(@Param("ids") List<Long> ids);

    /**
     * 更新菜品基本信息
     *
     * @param dish 更新后的菜品信息(包含Id)
     */
    @AutoFill(OperationType.UPDATE)
    void updateDish(@Param("dish") Dish dish);

    /**
     * 基于分类Id查询菜品信息
     *
     * @param categoryId 分类Id
     * @return 分类Id下的菜品集合
     */
    List<Dish> selectDishByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 查询指定分类下启售菜品的信息
     *
     * @param categoryId 分类Id
     * @return 分类Id下的启售菜品集合
     */
    List<Dish> selectStatus1DishByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 基于菜品状态查询菜品数量
     *
     * @param status 菜品状态
     * @return 指定状态的菜品数量
     */
    Integer selectDishCountByStatus(@Param("status") Integer status);
}
