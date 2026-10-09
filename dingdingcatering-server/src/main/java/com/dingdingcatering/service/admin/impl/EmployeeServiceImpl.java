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
        log.info("员工登录尝试: username={}", username);
        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            log.warn("员工登录失败: 账号不存在, username={}", username);
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(employee.getPassword())) {
            log.warn("员工登录失败: 密码错误, username={}", username);
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            log.warn("员工登录失败: 账号被锁定, username={}, name={}", username, employee.getName());
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

        log.info("员工登录成功: username={}, name={}, id={}", username, employee.getName(), employee.getId());
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
        log.info("新增员工: username={}, name={}", employeeDTO.getUsername(), employeeDTO.getName());
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD(employeeDTO.getIdNumber()).getBytes()));
        employeeMapper.addEmployee(employee);
        log.info("新增员工成功: id={}", employee.getId());
    }

    @Override
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        log.debug("分页查询员工: page={}, pageSize={}, name={}",
                employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize(), employeePageQueryDTO.getName());
        try (Page<Employee> employeePage = PageHelper.startPage(
                employeePageQueryDTO.getPage(),
                employeePageQueryDTO.getPageSize())) {

            List<Employee> list = employeeMapper.list(employeePageQueryDTO);
            log.debug("分页查询员工完成: total={}", employeePage.getTotal());
            return new PageResult<>(employeePage.getTotal(), list);
        }
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        log.info("更新员工状态: id={}, status={}", id, status);
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        employeeMapper.updateEmployee(employee);
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        log.info("编辑员工信息: id={}, username={}", employeeDTO.getId(), employeeDTO.getUsername());
        Employee employee = Employee.builder()
                .id(employeeDTO.getId())
                .username(employeeDTO.getUsername())
                .name(employeeDTO.getName())
                .phone(employeeDTO.getPhone())
                .sex(employeeDTO.getSex())
                .idNumber(employeeDTO.getIdNumber())
                .build();
        employeeMapper.updateEmployee(employee);
        log.info("编辑员工信息成功: id={}", employeeDTO.getId());
    }

    @Override
    public EmployeeDTO getEmployeeDTO(Long id) {
        log.debug("查询员工详情: id={}", id);
        Employee employee = employeeMapper.getById(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        log.info("修改密码: empId={}", BaseContext.getCurrentId());
        Function<String, String> toMD5 = password -> DigestUtils.md5DigestAsHex(password.getBytes());

        passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());
        if (employee == null) {
            log.warn("修改密码失败: 账号不存在, empId={}", passwordEditDTO.getEmpId());
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        String newPassword = toMD5.apply(passwordEditDTO.getNewPassword());
        if (newPassword.equals(employee.getPassword())) {
            log.warn("修改密码失败: 新旧密码相同, empId={}", passwordEditDTO.getEmpId());
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_ERROR + "新旧密码不能相同");
        }
        employee.setPassword(toMD5.apply(passwordEditDTO.getNewPassword()));
        employeeMapper.updateEmployee(employee);
        log.info("修改密码成功: empId={}", passwordEditDTO.getEmpId());
    }
}
