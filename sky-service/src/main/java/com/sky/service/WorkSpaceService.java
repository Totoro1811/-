package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: WorkSpaceService
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : B端-工作台信息服务层接口
 */
public interface WorkSpaceService {

    /**
     * 查询指定时间段销售与用户数据
     *
     * @return 指定时间段销售与用户数据Bean
     */
    BusinessDataVO selectBusinessData(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询今日订单数量数据
     *
     * @retur 今日订单数量数据
     */
    OrderOverViewVO selectOrdersOverview();

    /**
     * 查询菜品状态数据
     *
     * @return 全局通用返回信息Bean(菜品状态数据Bean)
     */
    DishOverViewVO selectDishOverView();

    /**
     * 查询套餐状态数据
     *
     * @return 全局通用返回信息Bean(套餐状态数据Bean)
     */
    SetmealOverViewVO selectSetmealOverView();
}
