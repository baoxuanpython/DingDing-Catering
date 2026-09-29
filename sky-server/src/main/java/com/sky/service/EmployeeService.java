package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import jakarta.validation.Valid;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 登录信息
     * @return 员工信息
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工注册
     * @param employeeDTO 注册信息
     */
    void addEmployee(EmployeeDTO employeeDTO);

    PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void updateStatus(Integer status, Long id);

    void updateEmployee(@Valid EmployeeDTO employeeDTO);

    EmployeeDTO getEmployeeDTO(Long id);

    void editPassword(@Valid PasswordEditDTO passwordEditDTO);

}
