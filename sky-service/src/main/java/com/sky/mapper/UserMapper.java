package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: UserMapper
 * USER: SHINIAN
 * DATE: 2023/5/21
 * DESCRIPTION : C端-用户信息持久层接口
 */
@Mapper
public interface UserMapper {
    /**
     * 基于OpenId查询用户信息
     *
     * @param openId 用户的OpenId(微信的用户唯一标识)
     * @return 查询到的用户信息(NULL : 没有查询到任何数据 非NULL : 查询到的数据)
     */
    User selectUserByOpenId(@Param("openId") String openId);

    /**
     * 添加用户信息
     *
     * @param user 用户信息
     */
    void insertUser(@Param("user") User user);

    /**
     * 基于用户Id查询用户信息
     *
     * @param userId 用户Id
     * @return 用户完整信息
     */
    User selectUserById(@Param("userId") Long userId);

    /**
     * 查询所有用户数据
     *
     * @return 所有用户完整信息的List集合
     */
    List<User> selectAllUserList();

    /**
     * 基于时间区间查询满足要求的用户数(新用户/总数)
     *
     * @param thisDayStartTime 开始时间
     * @param thisDayEndTime   结束时间
     * @return 新用户数量(如果一条数据的create_time处于开始时间到结束时间中, 这一条数据就是当天新注册的数据, 统计当天有多少数据新建 = > 有多少个新用户)
     *         总用户数量(如果一条数据的create_time小于结束时间中, 这一条数据就是当天前注册的数据, 统计有多少条数据的创建小于结束时间 = > 有多少个用户)
     */
    Integer selectCountByTime(@Param("startTime") LocalDateTime thisDayStartTime, @Param("endTime") LocalDateTime thisDayEndTime);
}
