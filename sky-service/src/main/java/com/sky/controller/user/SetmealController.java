package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealController
 * USER: SHINIAN
 * DATE: 2023/5/21
 * DESCRIPTION : C端-套餐信息表现层接口
 */
@Slf4j
@Api(tags = "C端-套餐信息表现层接口")
@RequestMapping("/user/setmeal")
@RestController("userSetmealController") //防止与B端的SetmealController在容器中的名称冲突
public class SetmealController {

    //注入套餐服务层接口实现类Bean
    @Resource
    private SetmealService setmealService;

    /**
     * 基于分类Id查询套餐信息
     *
     * @param categoryId 分类Id
     * @return 全局通用返回信息Bean(指定分类套餐List集合)
     */
    @ApiOperation("C端-基于分类Id查询套餐信息")
    @GetMapping("/list")
    @Cacheable(cacheNames = "SETMEAL_CACHE",key = "#categoryId")
    public Result<List<Setmeal>> selectSetmealByCategoryId(Long categoryId) {
        log.info("本次查询套餐信息的分类Id是 : {}", categoryId);
        //调用服务层进行数据查询
        List<Setmeal> setmealList = setmealService.selectSetmealByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 查询套餐中包含的菜品信息
     *
     * @param setmealId 套餐Id
     * @return 全局通用返回信息Bean(套餐中菜品的基本信息)
     */
    @ApiOperation("C端-查询套餐中包含的菜品信息")
    @GetMapping("/dish/{setmealId}")
    public Result<List<DishItemVO>> selectSetmealDishBySetmealId(@PathVariable Long setmealId) {
        log.info("本次要查询套餐包含菜品信息的套餐Id是 : {}", setmealId);
        //调用服务层传递setmealId查询结果
        List<DishItemVO> dishItemVOList = setmealService.selectSetmealDishBySetmealId(setmealId);
        return Result.success(dishItemVOList);
    }
}
