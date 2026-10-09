package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.annotation.AutoFill;
import com.dingdingcatering.dto.EmployeePageQueryDTO;
import com.dingdingcatering.entity.Employee;
import com.dingdingcatering.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill
    void addEmployee(Employee employee);

    List<Employee> list(EmployeePageQueryDTO employeePageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void updateEmployee(Employee employee);

    Employee getById(Long id);
}
