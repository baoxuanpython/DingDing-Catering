package com.dingdingcatering.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.dingdingcatering.constant.JwtClaimsConstant;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.constant.PasswordConstant;
import com.dingdingcatering.constant.StatusConstant;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.dto.EmployeeDTO;
import com.dingdingcatering.dto.EmployeeLoginDTO;
import com.dingdingcatering.dto.EmployeePageQueryDTO;
import com.dingdingcatering.dto.PasswordEditDTO;
import com.dingdingcatering.entity.Employee;
import com.dingdingcatering.exception.AccountLockedException;
import com.dingdingcatering.exception.AccountNotFoundException;
import com.dingdingcatering.exception.PasswordEditFailedException;
import com.dingdingcatering.exception.PasswordErrorException;
import com.dingdingcatering.mapper.admin.EmployeeMapper;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.EmployeeService;
import com.dingdingcatering.utils.JwtUtil;
import com.dingdingcatering.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final JwtProperties jwtProperties;

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, JwtProperties jwtProperties) {
        this.employeeMapper = employeeMapper;
        this.jwtProperties = jwtProperties;
    }

    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
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

        //3、返回登录对象
        return EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD(employeeDTO.getIdNumber()).getBytes()));
        employeeMapper.addEmployee(employee);
    }

    @Override
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        try (Page<Employee> employeePage = PageHelper.startPage(
                employeePageQueryDTO.getPage(),
                employeePageQueryDTO.getPageSize())) {

            List<Employee> list = employeeMapper.list(employeePageQueryDTO);

            return new PageResult<>(employeePage.getTotal(), list);
        }
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        employeeMapper.updateEmployee(employee);
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = Employee.builder()
                .id(employeeDTO.getId())
                .username(employeeDTO.getUsername())
                .name(employeeDTO.getName())
                .phone(employeeDTO.getPhone())
                .sex(employeeDTO.getSex())
                .idNumber(employeeDTO.getIdNumber())
                .build();
        employeeMapper.updateEmployee(employee);
    }

    @Override
    public EmployeeDTO getEmployeeDTO(Long id) {
        Employee employee = employeeMapper.getById(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        Function<String, String> toMD5 = password -> DigestUtils.md5DigestAsHex(password.getBytes());

        passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        String newPassword = toMD5.apply(passwordEditDTO.getNewPassword());
        if (newPassword.equals(employee.getPassword())) {
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_ERROR + "新旧密码不能相同");
        }
        employee.setPassword(toMD5.apply(passwordEditDTO.getNewPassword()));
        employeeMapper.updateEmployee(employee);
    }
}
