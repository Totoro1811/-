package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealDishMapper
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : B端-套餐菜品关联关系持久层
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 查询菜品套餐信息关联表中与ids集合中的id有关联关系的数据条数
     *
     * @param ids 要判断的菜品Id集合
     * @return 有关联关系的数据条数
     */
    public Long selectCountByDishIds(@Param("ids") List<Long> ids);

    /**
     * 添加套餐菜品关联数据
     *
     * @param setmealDishList 套餐菜品关联数据List集合
     */
    void batchInsertSetmealDish(@Param("setmealDishList") List<SetmealDish> setmealDishList);

    /**
     * 查询套餐中包含的菜品信息
     *
     * @param setmealId 套餐Id
     * @return 套餐中菜品的基本信息
     */
    List<DishItemVO> selectSetmealDishBySetmealId(@Param("setmealId") Long setmealId);
}
