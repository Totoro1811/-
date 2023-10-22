package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishController
 * USER: SHINIAN
 * DATE: 2023/5/21
 * DESCRIPTION : C端-菜品信息表现层接口
 */
@Slf4j
@Api(tags = "C端-菜品信息表现层接口")
@RequestMapping("/user/dish")
@RestController("userDishController") //防止与B端的DishController在容器中的名称冲突
public class DishController {
    //注入菜品服务层接口实现类Bean/Redis操作模板Bean
    @Resource
    private DishService dishService;
    @Resource
    private RedisTemplate redisTemplate;

    //声明Redis信息的Key模板
    private static String key = "DISH_CACHE::";

    /**
     * 查询指定分类菜品和口味
     *
     * @param categoryId 分类Id
     * @return 全局通用返回信息Bean(菜品基本信息 + 菜品的口味信息)
     */
    @ApiOperation("C端-查询指定分类菜品和口味")
    @GetMapping("/list")
    public Result<List<DishVO>> selectDishAndFlavorByCategoryId(Long categoryId) {
        log.info("本次查询菜品的分类Id是 : {}", categoryId);
        //查询Redis中是否有当前分类Id对应的缓存数据
        List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(key + categoryId);
        if (Objects.nonNull(dishVOList))
            return Result.success(dishVOList); //如果查询到缓存数据则直接返回
        //调用服务层查询对应数据
        dishVOList = dishService.selectDishAndFlavorByCategoryId(categoryId);
        //将数据查询到的数据保存到Redis数据库中
        redisTemplate.opsForValue().set(key + categoryId, dishVOList);
        return Result.success(dishVOList);
    }
}
