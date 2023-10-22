package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*
打开File -> Settings -> Editor -> File And Code Template -> Includes -> File Header 声明文件通用文档注释信息
/**
 * PROJECT_NAME: ${PROJECT_NAME}
 * NAME: ${NAME}
 * USER: ${USER}
 * DATE: ${DATE}
 * DESCRIPTION :
 */

/**
 * PROJECT_NAME: sky-take-out
 * NAME: EmployeeController
 * USER: SHINIAN
 * DATE: 2022/12/12
 * DESCRIPTION : B端-员工信息表现层接口
 */
@Slf4j
@Api(tags = "B端-员工信息表现层接口") //@Api:用户描述类的相关信息
@RestController
@RequestMapping("/admin/employee") //声明访问路径为/admin/employee自动映射到此方法
public class EmployeeController {

    //注入员工服务层实现类Bean
    @Autowired
    private EmployeeService employeeService;
    //注入Jwt令牌信息参数封装Bean(包含秘钥)
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录功能
     *
     * @param employeeLoginDTO 员工登录信息的DTO(用户名+密码)
     * @return 全局通用返回信息(员工登录响应信息VO : 包含Jwt令牌给前端用户下次访问携带)
     */
    @ApiOperation("B端-员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("B端员工登录参数 : {}", employeeLoginDTO);
        //基于服务层完成员工信息的查询
        Employee employee = employeeService.login(employeeLoginDTO);
        //根据查询出的用户信息生成Jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId()); //将员工Id保存到令牌部分(2)中
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(), //秘钥
                jwtProperties.getAdminTtl(), //过期时间
                claims); //令牌数据
        //封装员工登录响应信息VO(包含员工的基本信息+令牌数据)并返回
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId()).userName(employee.getUsername())
                .name(employee.getName()).token(token).build();
        return Result.success(employeeLoginVO);
    }

    /**
     * 退出登录功能(返回响应成功的Result即可,前端会自动将TOKEN令牌信息删除,下次请求不会携带)
     *
     * @return 全局通用返回信息
     */
    @ApiOperation("B端-员工退出登录")
    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }

    /**
     * 员工信息添加功能
     *
     * @param employeeDTO 用于接收员工(基本信息)的Bean
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("B端-添加员工信息")
    @PostMapping
    public Result insertEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工信息的参数数据 : {}", employeeDTO);
        //调用服务层传递employeeDTO进行添加
        employeeService.insertEmployee(employeeDTO);
        return Result.success(); //如果添加过程中出异常了,就交给全局异常处理器进行处理!
    }

    /**
     * 条件分页查询员工信息
     *
     * @param employeePageQueryDTO 分页查询的参数(name:员工姓名 page:页码 pageSize:每页显示条数)
     * @return 全局通用返回信息Bean(保存分页查询结果Bean ( 总条数 / 分页查询的数据集合))
     */
    @ApiOperation("B端-条件分页查询员工信息")
    @GetMapping("/page")
    public Result<PageResult> selectEmployeeByPage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("条件分页查询员工信息的参数是 : {}", employeePageQueryDTO);
        //调用服务层传递条件查询对象接收分页查询的PageResult结果
        PageResult employeePageResult = employeeService.selectEmployeeByPage(employeePageQueryDTO);
        return Result.success(employeePageResult);
    }

    /**
     * 更新员工账号状态
     *
     * @param status 更新后状态(启用的账号发的是0,禁用的账号发的是1)
     * @param id     员工Id
     * @return 全局通用返回信息
     */
    @ApiOperation("B端-更新员工账号状态")
    @PostMapping("/status/{status}")
    public Result modifyEmployeeStatus(@PathVariable Integer status, Long id) {
        log.info("更新员工账号状态的员工Id : {} , 更新后状态是 : {}", id, status);
        //调用服务层代码将参数封装为Employee对象后传递
        Employee employee = Employee.builder().id(id).status(status).build();
        employeeService.updateEmployee(employee);
        return Result.success();
    }

    /**
     * 基于员工Id查询员工信息
     *
     * @param id 要查询数据的员工Id
     * @return 全局通用返回信息(参数Id对应的员工完整信息)
     */
    @ApiOperation("B端-基于员工Id查询员工信息")
    @GetMapping("/{id}")
    public Result<Employee> selectEmployeeById(@PathVariable Long id) {
        log.info("本次要查询的员工信息的参数是 : {}", id);
        //调用服务层完成员工信息的查询
        Employee employee = employeeService.selectEmployeeById(id);
        return Result.success(employee);
    }

    /**
     * 更新员工基本信息
     *
     * @param employeeDTO 用于保存更新后的员工基本信息Bean(包含Id)
     * @return 全局通用返回信息
     */
    @ApiOperation("B端-更新员工基本信息方法")
    @PutMapping
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("更新员工基本信息的参数数据 : {}", employeeDTO);
        //将EmployeeDTO转换为Employee(过滤无用数据) => 方便作为参数传递给服务层
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeService.updateEmployee(employee);
        return Result.success();
    }

    /**
     * 更改员工登录密码
     *
     * @param passwordEditDTO 用于保存员工原始密码与修改后密码的Bean
     * @return 全局通用返回信息
     */
    @ApiOperation("B端-更改员工登录密码")
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("本次员工密码修改前内容 : {} , 修改后内容 : {}", passwordEditDTO.getOldPassword(), passwordEditDTO.getNewPassword());
        //将当前登录的员工Id封装到passwordEditDTO中传递给服务层
        passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
