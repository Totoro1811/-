package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealServiceImpl
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : B端-套餐信息服务层接口实现类
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    //注入套餐持久层接口实现Bean
    @Resource
    private SetmealMapper setmealMapper;
    //注入菜品持久层接口实现Bean
    @Resource
    private DishMapper dishMapper;
    //注入套餐菜品持久层接口实现Bean
    @Resource
    private SetmealDishMapper setmealDishMapper;

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO 套餐分页查询信息封装Bean
     * @return 全局通用返回信息Bean(分页结果封装Bean)
     */
    @Override
    public PageResult selectByPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        //开启分页查询拦截
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.selectByPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加套餐信息
     *
     * @param setmealDTO 套餐信息封装Bean(包含套餐基本信息与套餐内菜品信息)
     */
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void insertSetmeal(SetmealDTO setmealDTO) {
        //创建套餐基本信息完成信息获取转换后添加
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insertSetmeal(setmeal);
        //获取套餐信息中的具体菜品组成信息List后添加套餐Id后批量添加
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        setmealDishList.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.batchInsertSetmealDish(setmealDishList);
    }

    /**
     * 删除套餐信息
     *
     * @param ids 要删除的套餐Id组成的List集合
     */
    @Transactional
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void batchDeleteSetmeal(List<Long> ids) {
        for (Long deleteSetmealId : ids) {
            //查询当前套餐Id的状态(如果是启售状态则无法删除)
            Integer setmealStatus = setmealMapper.selectStatusById(deleteSetmealId);
            if (setmealStatus.equals(StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            //删除套餐信息与套餐菜品表信息
            setmealMapper.deleteById(deleteSetmealId);
            setmealMapper.deleteSetmealDishBySetmealId(deleteSetmealId);
        }
    }

    /**
     * 基于套餐Id查询套餐信息
     *
     * @param setmealId 套餐Id
     * @return 套餐与套餐菜品关联VO
     */
    @Override
    public SetmealVO selectById(Long setmealId) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.selectById(setmealId);
        List<SetmealDish> setmealDishList = setmealMapper.selectSetmealDishBySetmealId(setmealId);
        //将查询出的基本信息封装到VO中并将查询出的对应套餐菜品关联信息保存到VO中
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    /**
     * 更新套餐信息
     *
     * @param setmealDTO 套餐信息封装Bean(包含套餐基本信息与套餐内菜品信息【包含SetmealId】)
     */
    @Transactional
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void updateSetmeal(SetmealDTO setmealDTO) {
        //创建Setmeal从SetmealDTO获取更新后的信息完成更新
        Setmeal newSetmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, newSetmeal);
        setmealMapper.updateSetmeal(newSetmeal);
        //删除当前Setmeal的Id在套餐信息表中对应的所有菜品信息后重新添加
        setmealMapper.deleteSetmealDishBySetmealId(newSetmeal.getId());
        if (Objects.nonNull(setmealDTO.getSetmealDishes()) && !setmealDTO.getSetmealDishes().isEmpty()) {
            setmealDTO.getSetmealDishes().forEach(setmealDish -> setmealDish.setSetmealId(newSetmeal.getId()));
            setmealDishMapper.batchInsertSetmealDish(setmealDTO.getSetmealDishes());
        }
    }

    /**
     * 更新套餐状态(出售/禁售)
     *
     * @param setmealId 套餐Id
     * @param status    更新后的状态
     */
    @Override
    @CacheEvict(cacheNames = "SETMEAL_CACHE", allEntries = true) //删除所有前缀是SETMEAL_CACHE的缓存数据
    public void modifyStatus(Long setmealId, Integer status) {
        //将状态信息与套餐Id封装为Setmeal
        Setmeal newSetmeal = Setmeal.builder().id(setmealId).status(status).build();
        if (newSetmeal.getStatus() == 0) {
            //如果修改为禁售则无需查询菜品状态
            setmealMapper.updateSetmeal(newSetmeal);
        } else if (newSetmeal.getStatus() == 1) {
            //如果要进行套餐启售,必须先查询当前套餐关联的所有菜品是否启售
            List<SetmealDish> setmealDishList = setmealMapper.selectSetmealDishBySetmealId(setmealId);
            setmealDishList.forEach(setmealDish -> {
                Dish dish = dishMapper.selectDishById(setmealDish.getDishId());
                if (dish.getStatus().equals(StatusConstant.DISABLE))
                    throw new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
            });
            //如果所有菜品都是启售状态则套餐可以启售
            setmealMapper.updateSetmeal(newSetmeal);
        }
    }

    /**
     * 基于分类Id查询套餐信息
     *
     * @param categoryId 分类Id
     * @return 指定分类套餐List集合
     */
    @Override
    public List<Setmeal> selectSetmealByCategoryId(Long categoryId) {
        //封装用于查询套餐信息的Setmeal条件
        Setmeal conditionSetmeal = Setmeal.builder().status(1).categoryId(categoryId).build();
        //调用持久层传递保存了查询参数的条件对象进行查询
        List<Setmeal> setmealList = setmealMapper.selectByCondition(conditionSetmeal);
        return setmealList;
    }

    /**
     * 查询套餐中包含的菜品信息
     *
     * @param setmealId 套餐Id
     * @return 套餐中菜品的基本信息
     */
    @Override
    public List<DishItemVO> selectSetmealDishBySetmealId(Long setmealId) {
        //调用套餐菜品关联持久层查询套餐中菜品的基本信息
        List<DishItemVO> dishItemVOList = setmealDishMapper.selectSetmealDishBySetmealId(setmealId);
        return dishItemVOList;
    }
}
