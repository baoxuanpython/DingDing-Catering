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

    /**
     * 查询指定员工的密码密文。
     * <p>
     * 密码属于敏感字段，{@link #getById(Long)} 与列表查询均刻意不返回该字段，
     * 需要校验密码的场景必须走本方法单独查询。
     *
     * @param id 员工ID
     * @return 密码密文；员工不存在时返回 null
     */
    @Select("select password from employee where id = #{id}")
    String getPasswordById(Long id);

    void deleteBatch(List<Long> ids);
}