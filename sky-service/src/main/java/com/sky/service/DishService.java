package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishService
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : B端-菜品信息服务层接口
 */
public interface DishService {
    /**
     * 添加菜品数据(包含菜品口味)
     *
     * @param dishDTO 用于接收菜品数据与菜品口味数据的JavaBean
     */
    void insertDish(DishDTO dishDTO);

    /**
     * 分页条件查询菜品数据
     *
     * @param dishPageQueryDTO 分页查询的条件(页码/分页条数/菜品名称/分类Id/菜品状态)
     * @return 分页查询结果封装Bean ( 总条数 / 分页查询的结果List)
     */
    PageResult selectDishByPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品信息
     *
     * @param ids 保存了要删除的菜品Id的集合
     */
    void batchDeleteDish(List<Long> ids);

    /**
     * 基于菜品Id查询菜品信息(包含口味)
     *
     * @param dishId 菜品Id
     * @return 菜品完整信息[基本信息 + 口味信息]
     */
    DishVO selectDishVOById(Long dishId);

    /**
     * 更新菜品完整信息(包含口味)
     *
     * @param dishDTO 更新后的菜品完整信息Bean(▲包含菜品Id)
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 修改菜品状态(启售/停售)
     *
     * @param dish 保存了修改后状态与菜品Id的Bean
     */
    void modifyStatus(Dish dish);

    /**
     * 基于分类Id查询菜品信息
     *
     * @param categoryId 分类Id
     * @return 分类Id下的菜品集合
     */
    List<Dish> selectDishByCategoryId(Long categoryId);

    /**
     * 查询指定分类菜品和口味
     *
     * @param categoryId 分类Id
     * @return 菜品基本信息 + 菜品的口味信息的集合
     */
    List<DishVO> selectDishAndFlavorByCategoryId(Long categoryId);
}
