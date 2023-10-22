package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealService
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : B端-套餐信息服务层接口
 */
public interface SetmealService {
    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO 套餐分页查询信息封装Bean
     * @return 全局通用返回信息Bean(分页结果封装Bean)
     */
    PageResult selectByPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 添加套餐信息
     *
     * @param setmealDTO 套餐信息封装Bean(包含套餐基本信息与套餐内菜品信息)
     */
    @Transactional
    void insertSetmeal(SetmealDTO setmealDTO);

    /**
     * 删除套餐信息
     *
     * @param ids 要删除的套餐Id组成的List集合
     */
    @Transactional
    void batchDeleteSetmeal(List<Long> ids);

    /**
     * 基于套餐Id查询套餐信息
     *
     * @param setmealId 套餐Id
     * @return 套餐与套餐菜品关联VO
     */
    SetmealVO selectById(Long setmealId);

    /**
     * 更新套餐信息
     *
     * @param setmealDTO 套餐信息封装Bean(包含套餐基本信息与套餐内菜品信息【包含SetmealId】)
     */
    @Transactional
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 更新套餐状态(出售/禁售)
     *
     * @param status 更新后的状态
     * @param setmealId     套餐Id
     */
    void modifyStatus(Long setmealId, Integer status);

    /**
     * 基于分类Id查询套餐信息
     *
     * @param categoryId 分类Id
     * @return 指定分类套餐List集合
     */
    List<Setmeal> selectSetmealByCategoryId(Long categoryId);

    /**
     * 查询套餐中包含的菜品信息
     *
     * @param setmealId 套餐Id
     * @return 套餐中菜品的基本信息
     */
    List<DishItemVO> selectSetmealDishBySetmealId(Long setmealId);
}
