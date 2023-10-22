package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: EmployeeMapper
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : B端-员工信息持久层接口
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 基于员工姓名查询员工信息方法
     *
     * @param username 员工登录信息的用户名称
     * @return 查询到的员工信息（可能为NULL）
     */
    Employee selectEmployeeByUsername(@Param("username") String username);

    /**
     * 员工信息添加功能
     *
     * @param employee 保存员工完整信息的Bean
     */
    @AutoFill(OperationType.INSERT)
    void insertEmployee(@Param("employee") Employee employee);

    /**
     * 条件分页查询员工信息
     *
     * @param employeePageQueryDTO 分页查询的参数(name:员工姓名 page:页码 pageSize:每页显示条数)
     * @return PageHelper保存分页查询结果Bean
     */
    Page<Employee> selectEmployeeByPage(@Param("employeePageQueryDTO") EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工信息 => 就认为employee的成员变量有值就是要修改,是NULL值就表示不修改!
     *
     * @param employee 更新后的员工信息(▲其他字段并不关心是否有值 Id字段必须有值)
     */
    @AutoFill(OperationType.UPDATE)
    void updateEmployee(@Param("employee") Employee employee);

    /**
     * 基于员工Id查询员工信息
     *
     * @param id 要查询数据的员工Id
     * @return 参数Id对应的员工完整信息
     */
    Employee selectEmployeeById(@Param("id") Long id);
}
