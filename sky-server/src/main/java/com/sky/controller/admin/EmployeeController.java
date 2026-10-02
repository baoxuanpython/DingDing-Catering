package com.sky.controller.admin;

import com.sky.annotation.AutoLogDTO;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.EmployeeService;
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
    @AutoLogDTO("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {

        Employee employee = employeeService.login(employeeLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());
        claims.put(JwtClaimsConstant.NAME, employee.getName());
        claims.put(JwtClaimsConstant.PHONE, employee.getPhone());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

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
    @AutoLogDTO("员工退出")
    public Result<String> logout() {
        return Result.success("退出登录");
    }

    @PostMapping
    @AutoLogDTO("新增员工")
    public Result<String> addEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    @AutoLogDTO("分页查询员工")
    public Result<PageResult<Employee>> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        return Result.success(employeeService.pageQuery(employeePageQueryDTO));
    }

    @PostMapping("/status/{status}")
    @AutoLogDTO("更新员工状态")
    public Result<Void> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        employeeService.updateStatus(status, id);
        return Result.success();
    }

    @PutMapping
    @AutoLogDTO("编辑员工信息")
    public Result<Void> updateEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @AutoLogDTO("查询员工详情")
    public Result<EmployeeDTO> getEmployee(@PathVariable Long id) {
        return Result.success(employeeService.getEmployeeDTO(id));
    }

    @PutMapping("editPassword")
    @AutoLogDTO("修改密码")
    public Result<Void> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
