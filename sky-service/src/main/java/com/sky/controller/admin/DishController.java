package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishController
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : B端-菜品信息表现层接口
 */
@Slf4j
@Api(tags = "B端-菜品信息表现层接口")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    //注入菜品信息服务层接口实现Bean
    @Resource
    private DishService dishService;

    /**
     * 添加菜品数据(包含菜品口味)
     *
     * @param dishDTO 用于接收菜品数据与菜品口味数据的JavaBean
     * @return 全局通用返回结果Bean
     */
    @ApiOperation("B端-添加菜品数据")
    @PostMapping
    public Result insertDish(@RequestBody DishDTO dishDTO) {
        log.info("菜品数据添加的参数信息 : {}", dishDTO);
        //调用菜品信息服务层传递dishDTO进行数据添加
        dishService.insertDish(dishDTO);
        return Result.success();
    }

    /**
     * 分页条件查询菜品数据
     *
     * @param dishPageQueryDTO 分页查询的条件(页码/分页条数/菜品名称/分类Id/菜品状态)
     * @return 全局通用返回信息Bean(分页查询结果封装Bean ( 总条数 / 分页查询的结果List))
     */
    @ApiOperation("B端-分页条件查询菜品数据")
    @GetMapping("/page")
    public Result<PageResult> selectDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页条件查询菜品信息的参数内容 : {}", dishPageQueryDTO);
        //调用服务层传递dishPageQueryDTO完成查询获取结果
        PageResult dishPageResult = dishService.selectDishByPage(dishPageQueryDTO);
        return Result.success(dishPageResult);
    }

    /**
     * 批量删除菜品信息
     *
     * @param ids 保存了要删除的菜品Id的集合
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("B端-批量删除菜品信息")
    @DeleteMapping
    public Result batchDeleteDish(@RequestParam List<Long> ids) {
        log.info("批量删除的菜品Id集合是 : {}", ids);
        //调用服务层传递ids集合
        dishService.batchDeleteDish(ids);
        return Result.success();
    }

    /**
     * 基于菜品Id查询菜品信息(包含口味)
     *
     * @param dishId 菜品Id
     * @return 全局通用返回信息Bean(菜品完整信息[基本信息 + 口味信息])
     */
    @ApiOperation("B端-基于菜品Id查询菜品信息(包含口味)")
    @GetMapping("/{dishId}")
    public Result<DishVO> selectDishVOById(@PathVariable Long dishId) {
        log.info("基于菜品Id查询菜品信息的参数是 : {}", dishId);
        //调用服务层传递菜品Id接收DishVO结果并返回
        DishVO dishVO = dishService.selectDishVOById(dishId);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品完整信息(包含口味)
     *
     * @param dishDTO 更新后的菜品完整信息Bean(▲包含菜品Id)
     * @return 全局通用返回Bean
     */
    @ApiOperation("B端-更新菜品完整信息(包含口味)")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("更新后的菜品完整参数信息 : {}", dishDTO);
        //调用服务层传递dishDTO完整更新
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 菜品的启售/停售功能
     *
     * @param status 修改后的菜品状态
     * @param id     菜品Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("B端-菜品的启售/停售功能")
    @PostMapping("/status/{status}")
    public Result modifyStatus(@PathVariable Integer status, Long id) {
        log.info("本次要修改状态的菜品Id : {} , 修改的状态是 : {}", id, status);
        //将参数封装为一个Dish对象传递给服务层
        Dish dish = Dish.builder().id(id).status(status).build();
        dishService.modifyStatus(dish);
        return Result.success();
    }

    /**
     * 基于分类Id查询菜品信息
     *
     * @param categoryId 分类Id
     * @return 全局通用返回Bean(分类Id下的菜品集合)
     */
    @ApiOperation("B端-基于分类Id查询菜品信息")
    @GetMapping("/list")
    public Result<List<Dish>> selectDishByCategoryId(Long categoryId) {
        log.info("本次基于分类Id查询菜品信息的参数是 : {}", categoryId);
        //调用服务层传递分类Id查询菜品信息
        List<Dish> dishList = dishService.selectDishByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
