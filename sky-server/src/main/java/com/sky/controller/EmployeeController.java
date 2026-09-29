package com.sky.controller;

import com.sky.constant.JwtClaimsConstant;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {


    private final EmployeeService employeeService;

    private final JwtProperties jwtProperties;

    public EmployeeController(EmployeeService employeeService, JwtProperties jwtProperties) {
        this.employeeService = employeeService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO 登录参数
     * @return 登录成功后的员工信息
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        log.info("员工登录成功：{}", employee);
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());
        claims.put(JwtClaimsConstant.NAME, employee.getName());
        claims.put(JwtClaimsConstant.PHONE, employee.getPhone());
        log.info("登录密钥:{}", jwtProperties.getAdminSecretKey());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        log.info("登录令牌:{}", token);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return 退出成功后的员工信息
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出登录");
    }

    @PostMapping
    public Result<String> addEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    public Result<PageResult<Employee>> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        return Result.success(employeeService.pageQuery(employeePageQueryDTO));
    }

    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        employeeService.updateStatus(status, id);
        return Result.success(status == 1 ? "启用成功" : "停用成功");
    }

    @PutMapping
    public Result<String> updateEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeDTO);
        return Result.success("更新成功");
    }

    @GetMapping("/{id}")
    public Result<EmployeeDTO> getEmployee(@PathVariable Long id) {
        return Result.success(employeeService.getEmployeeDTO(id));
    }

    @PutMapping("editPassword")
    public Result<String> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        employeeService.editPassword(passwordEditDTO);
        return Result.success("密码更新成功");
    }
}
