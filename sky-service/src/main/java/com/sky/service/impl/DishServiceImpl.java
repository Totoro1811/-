package com.sky.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishServiceImpl
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : B端-分类信息服务层接口实现类
 */
@Service
public class DishServiceImpl implements DishService {

    //注入菜品信息/菜品口味信息/套餐菜品关联关系持久层接口实现类Bean
    @Resource
    private DishMapper dishMapper;
    @Resource
    private DishFlavorMapper dishFlavorMapper;
    @Resource
    private SetmealDishMapper setmealDishMapper;

    /**
     * 添加菜品数据(包含菜品口味)
     *
     * @param dishDTO 用于接收菜品数据与菜品口味数据的JavaBean
     */
    @Transactional //菜品信息与菜品口味信息添加要么一起成功,要么一起失败!
    @Override
    public void insertDish(DishDTO dishDTO) {
        //创建菜品信息对象(用于保存DTO中的菜品原始信息)
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.ENABLE); //完成菜品默认状态的指定
        //调用持久层添加菜品基本信息
        dishMapper.insertDish(dish);
        //判断本次菜品数据是否包含菜品口味数据(包含则添加,不包含则不添加,如果不判断,则会出现SQL语句语法错误的问题)
        if (Objects.nonNull(dishDTO.getFlavors()) && dishDTO.getFlavors().size() > 0) {
            //处理DishDTO中的菜品口味信息 -> 默认将菜品口味对象封装好了(口味名称/口味数据)没有对应的菜品Id -> 从dish对象中获取
            Long dishId = dish.getId();
            dishDTO.getFlavors().forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //将菜品口味集合交给菜品口味持久层进行批量添加
            dishFlavorMapper.batchInsertDishFlavor(dishDTO.getFlavors());
        }
        this.clearDishCache(); //清空菜品缓存
    }

    /**
     * 分页条件查询菜品数据
     *
     * @param dishPageQueryDTO 分页查询的条件(页码/分页条数/菜品名称/分类Id/菜品状态)
     * @return 分页查询结果封装Bean ( 总条数 / 分页查询的结果List)
     */
    @Override
    public PageResult selectDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        //基于PageHelper开启分页
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //调用持久层传递dishPageQueryDTO,声明返回的数据类型为DishVO[包含了分类名称] / Dish不包含分类名称
        Page<DishVO> dishVOPage = dishMapper.selectDishByPage(dishPageQueryDTO);
        return PageResult.builder().total(dishVOPage.getTotal()).records(dishVOPage.getResult()).build();
    }

    /**
     * 批量删除菜品信息
     *
     * @param ids 保存了要删除的菜品Id的集合
     */
    @Transactional
    @Override
    public void batchDeleteDish(List<Long> ids) {
        //判断所有的要删除的菜品Id是否是启售状态 是:抛异常 没有:继续完成其他判断
        for (Long dishId : ids) {
            Dish dish = dishMapper.selectDishById(dishId);
            if (dish.getStatus().equals(StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //判断菜品套餐关联中是否存在与当前的Id有关联关系的数据 => 有:抛异常 没有:则完成删除
        Long count = setmealDishMapper.selectCountByDishIds(ids);
        if (count > 0)
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        //如果以上的操作完成后均未抛出异常(满足删除前提)
        dishMapper.batchDeleteDish(ids);
        dishFlavorMapper.batchDeleteDishFlavor(ids);
        this.clearDishCache(); //清空菜品缓存
    }

    /**
     * 基于菜品Id查询菜品信息(包含口味)
     *
     * @param dishId 菜品Id
     * @return 菜品完整信息[基本信息 + 口味信息]
     */
    @Override
    public DishVO selectDishVOById(Long dishId) {
        //基于菜品Id查询菜品基本信息
        Dish dish = dishMapper.selectDishById(dishId);
        //基于菜品Id查询菜品口味信息
        List<DishFlavor> dishFlavorList = dishFlavorMapper.selectDishFlavorByDishId(dishId);
        //将基本信息与口味信息封装为一个DishVO对象返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO); //将dish中的基本信息拷贝给dishVO
        dishVO.setFlavors(dishFlavorList); //将菜品对应的口味信息集合封装到dishVO中
        return dishVO;
    }

    /**
     * 更新菜品完整信息(包含口味)
     *
     * @param dishDTO 更新后的菜品完整信息Bean(▲包含菜品Id)
     */
    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        //获取DishDTO中菜品基本信息完成更新
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDish(dish);
        //获取DishDTO中菜品的口味信息完成删除后重新添加
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        //删除掉菜品口味表中当前Dish的口味信息(无论本次是否有口味信息)
        dishFlavorMapper.batchDeleteDishFlavor(ListUtil.of(dish.getId()));
        if (Objects.nonNull(dishFlavorList) && dishFlavorList.size() > 0) {
            //将dishFlavorList中的菜品口味数据重新添加到集合中 [重新给每一个菜品口味赋值一个DishId(老口味会携带DishId,新的口味不会有)]
            dishFlavorList.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.batchInsertDishFlavor(dishFlavorList);
        }
        this.clearDishCache(); //清空菜品缓存
    }

    /**
     * 修改菜品状态(启售/停售)
     *
     * @param dish 保存了修改后状态与菜品Id的Bean
     */
    @Override
    public void modifyStatus(Dish dish) {
        //调用持久层的更新菜品基本信息方法
        dishMapper.updateDish(dish);
        this.clearDishCache(); //清空菜品缓存
    }

    /**
     * 基于分类Id查询菜品信息
     *
     * @param categoryId 分类Id
     * @return 分类Id下的菜品集合
     */
    @Override
    public List<Dish> selectDishByCategoryId(Long categoryId) {
        //调用服务层查询分类下的菜品信息集合
        return dishMapper.selectDishByCategoryId(categoryId);
    }

    /**
     * 查询指定分类菜品和口味
     *
     * @param categoryId 分类Id
     * @return 菜品基本信息 + 菜品的口味信息的集合
     */
    @Override
    public List<DishVO> selectDishAndFlavorByCategoryId(Long categoryId) {
        //调用持久层查询所有该分类启售菜品的信息
        List<Dish> dishList = dishMapper.selectStatus1DishByCategoryId(categoryId);
        return dishList.stream().map(dish -> {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO); //将Dish中的基础信息拷贝到DishVO
            List<DishFlavor> dishFlavorList = dishFlavorMapper.selectDishFlavorByDishId(dish.getId());
            dishVO.setFlavors(dishFlavorList);
            return dishVO;
        }).collect(Collectors.toList());
    }

    //注入Redis操作模板类
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 清空菜品缓存数据
     */
    private void clearDishCache() {
        //查询所有满足DISH_CACHE::*模板的KEY(要删除掉)
        Set keys = redisTemplate.keys("DISH_CACHE::*");
        //基于delete方法将要删除的Key组成的单列集合作为参数传递,即可删除!
        redisTemplate.delete(keys);
    }
}
