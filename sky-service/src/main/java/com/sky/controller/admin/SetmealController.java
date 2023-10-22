package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealController
 * USER: SHINIAN
 * DATE: 2023/5/18
 * DESCRIPTION :B端-套餐表现层接口
 */
@Slf4j
@Api(tags = "B端-套餐表现层接口")
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    //注入套餐服务层接口实现Bean
    @Resource
    private SetmealService setmealService;

    /**
     * 分页查询套餐信息功能
     *
     * @param setmealPageQueryDTO 套餐分页查询信息封装Bean
     * @return 全局通用返回信息Bean(分页结果封装Bean)
     */
    @ApiOperation("Setmeal套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> selectByPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐信息的参数信息 : {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.selectByPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 添加套餐信息功能
     *
     * @param setmealDTO 套餐信息封装Bean(包含套餐基本信息与套餐内菜品信息)
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("Setmeal添加套餐信息")
    @PostMapping
    public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("添加套餐的参数信息 : {}", setmealDTO);
        setmealService.insertSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 删除套餐信息功能
     *
     * @param ids 要删除的套餐Id组成的List集合
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("Setmeal删除套餐信息")
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam List<Long> ids) {
        log.info("删除套餐的参数信息 : {}", ids);
        setmealService.batchDeleteSetmeal(ids);
        return Result.success();
    }

    /**
     * 基于套餐Id查询套餐信息功能
     *
     * @param id 套餐Id
     * @return 全局通用返回信息Bean(套餐与套餐菜品关联VO)
     */
    @ApiOperation("Setmeal查询套餐信息")
    @GetMapping("/{id}")
    public Result<SetmealVO> selectById(@PathVariable Long id) {
        log.info("基于Id查询套餐信息的参数信息 : {}", id);
        SetmealVO setmealVO = setmealService.selectById(id);
        return Result.success(setmealVO);
    }

    /**
     * 更新套餐信息功能
     *
     * @param setmealDTO 套餐信息封装Bean(包含套餐基本信息与套餐内菜品信息【包含SetmealId】)
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("Setmeal更新套餐信息")
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("更新套餐信息的参数信息 : {}", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 更新套餐状态(出售/禁售)功能
     *
     * @param status 更新后的状态
     * @param id     套餐Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("Setmeal修改状态")
    @PostMapping("/status/{status}")
    public Result modifyStatus(Long id, @PathVariable Integer status) {
        log.info("修改套餐状态的参数信息 套餐Id : {},状态信息 : {}", id, status);
        setmealService.modifyStatus(id, status);
        return Result.success();
    }
}
