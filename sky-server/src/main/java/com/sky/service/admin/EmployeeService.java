package com.sky.service.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import com.sky.vo.EmployeeLoginVO;
import jakarta.validation.Valid;

public interface EmployeeService {

    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employeeDTO);

    PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void updateStatus(Integer status, Long id);

    void updateEmployee(@Valid EmployeeDTO employeeDTO);

    EmployeeDTO getEmployeeDTO(Long id);

    void editPassword(@Valid PasswordEditDTO passwordEditDTO);

}
