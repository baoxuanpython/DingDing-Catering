package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.EmployeeDTO;
import com.dingdingcatering.dto.EmployeeLoginDTO;
import com.dingdingcatering.dto.EmployeePageQueryDTO;
import com.dingdingcatering.dto.PasswordEditDTO;
import com.dingdingcatering.entity.Employee;
import com.dingdingcatering.result.PageResult;

import com.dingdingcatering.vo.EmployeeLoginVO;
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
