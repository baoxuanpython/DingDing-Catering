package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.EmployeeDTO;
import com.dingdingcatering.dto.EmployeeLoginDTO;
import com.dingdingcatering.dto.EmployeePageQueryDTO;
import com.dingdingcatering.dto.PasswordEditDTO;
import com.dingdingcatering.entity.Employee;
import com.dingdingcatering.result.PageResult;

import com.dingdingcatering.vo.EmployeeLoginVO;
import jakarta.validation.Valid;

/**
 * 员工管理服务接口
 */
public interface EmployeeService {

    /**
     * 员工登录。
     *
     * @param employeeLoginDTO 登录信息
     * @return 登录结果（含JWT令牌）
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工。
     *
     * @param employeeDTO 员工信息
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询。
     *
     * @param employeePageQueryDTO 分页查询参数
     * @return 员工分页数据
     */
    PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工账号状态。
     *
     * @param status 目标状态
     * @param id     员工ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 更新员工信息。
     *
     * @param employeeDTO 待更新的员工信息
     */
    void updateEmployee(@Valid EmployeeDTO employeeDTO);

    /**
     * 根据ID查询员工DTO。
     *
     * @param id 员工ID
     * @return 员工DTO
     */
    EmployeeDTO getEmployeeDTO(Long id);

    /**
     * 修改密码。
     *
     * @param passwordEditDTO 含旧密码与新密码
     */
    void editPassword(@Valid PasswordEditDTO passwordEditDTO);

}
