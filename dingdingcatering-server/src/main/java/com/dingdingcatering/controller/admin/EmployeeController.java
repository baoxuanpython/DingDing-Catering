package com.dingdingcatering.controller.admin;

import com.dingdingcatering.annotation.AutoLogDTO;
import com.dingdingcatering.constant.JwtClaimsConstant;
import com.dingdingcatering.dto.EmployeeDTO;
import com.dingdingcatering.dto.EmployeeLoginDTO;
import com.dingdingcatering.dto.EmployeePageQueryDTO;
import com.dingdingcatering.dto.PasswordEditDTO;
import com.dingdingcatering.entity.Employee;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.EmployeeService;
import com.dingdingcatering.utils.JwtUtil;
import com.dingdingcatering.vo.EmployeeLoginVO;
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


    public EmployeeController(EmployeeService employeeService, JwtProperties jwtProperties) {
        this.employeeService = employeeService;
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
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }
    @PostMapping("/logout")
    @AutoLogDTO("员工退出登录")
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
