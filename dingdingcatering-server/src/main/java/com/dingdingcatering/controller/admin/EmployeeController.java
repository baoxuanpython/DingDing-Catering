package com.dingdingcatering.controller.admin;

import com.dingdingcatering.dto.EmployeeDTO;
import com.dingdingcatering.dto.EmployeeLoginDTO;
import com.dingdingcatering.dto.EmployeePageQueryDTO;
import com.dingdingcatering.dto.PasswordEditDTO;
import com.dingdingcatering.entity.Employee;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.EmployeeService;
import com.dingdingcatering.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 员工管理控制器
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
     * 员工登录接口。
     * <p>
     * 验证账号密码，生成 JWT 令牌并返回给前端。
     *
     * @param employeeLoginDTO 员工登录信息
     * @return 包含 JWT 令牌的登录结果
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }

    /**
     * 员工退出登录接口。
     *
     * @return 退出成功提示
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出登录");
    }

    /**
     * 新增员工接口。
     *
     * @param employeeDTO 员工信息（含校验注解）
     * @return 添加成功提示
     */
    @PostMapping
    public Result<String> addEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return Result.success("添加成功");
    }

    /**
     * 员工分页查询接口。
     *
     * @param employeePageQueryDTO 分页查询参数（含姓名模糊查询）
     * @return 员工分页数据
     */
    @GetMapping("/page")
    public Result<PageResult<Employee>> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        return Result.success(employeeService.pageQuery(employeePageQueryDTO));
    }

    /**
     * 更新员工账号状态接口。
     *
     * @param status 目标状态（启用/禁用）
     * @param id     用于定位目标员工的主键标识
     * @return 更新成功提示
     */
    @PostMapping("/status/{status}")
    public Result<Void> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        employeeService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 编辑员工信息接口。
     *
     * @param employeeDTO 待更新的员工信息（含校验注解）
     * @return 更新成功提示
     */
    @PutMapping
    public Result<Void> updateEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 根据ID查询员工详情接口。
     *
     * @param id 用于查询员工的唯一标识
     * @return 员工信息DTO
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmployee(@PathVariable Long id) {
        return Result.success(employeeService.getEmployee(id));
    }

    /**
     * 员工修改密码接口。
     *
     * @param passwordEditDTO 含旧密码与新密码的传输对象
     * @return 修改成功提示
     */
    @PutMapping("editPassword")
    public Result<Void> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
