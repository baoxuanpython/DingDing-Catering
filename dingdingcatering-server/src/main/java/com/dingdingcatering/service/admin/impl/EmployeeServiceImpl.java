package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.constant.JwtClaimsConstant;
import com.dingdingcatering.constant.PasswordConstant;
import com.dingdingcatering.constant.StatusConstant;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.dto.EmployeeDTO;
import com.dingdingcatering.dto.EmployeeLoginDTO;
import com.dingdingcatering.dto.EmployeePageQueryDTO;
import com.dingdingcatering.dto.PasswordEditDTO;
import com.dingdingcatering.dto.PasswordSetDTO;
import com.dingdingcatering.entity.Employee;
import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.exception.*;
import com.dingdingcatering.mapper.admin.EmployeeMapper;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.EmployeeService;
import com.dingdingcatering.utils.JwtUtil;
import com.dingdingcatering.vo.EmployeeLoginVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.*;

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
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(employee.getPassword())) {
            log.warn("员工登录失败: 密码错误, username={}", username);
            //密码错误
            throw new PasswordErrorException(ErrorCode.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            log.warn("员工登录失败: 账号被禁用, username={}, name={}", username, employee.getName());
            //账号被禁用
            throw new AccountDisabledException(ErrorCode.ACCOUNT_DISABLED);
        }
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMPID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());
        claims.put(JwtClaimsConstant.EMPNAME, employee.getName());
        String role = Objects.equals(employee.getUsername(), JwtClaimsConstant.ADMIN)
                ? JwtClaimsConstant.ADMIN : JwtClaimsConstant.USER;
        claims.put(JwtClaimsConstant.ROLE, role);
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
                .role(role)
                .token(token)
                .build();
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        requireAdmin();
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
            boolean admin = isAdmin();
            list.forEach(employee -> {
                if (!admin) {
                    employee.setPhone(maskPhone(employee.getPhone()));
                    employee.setIdNumber(maskIdNumber(employee.getIdNumber()));
                }
            });
            log.debug("分页查询员工完成: total={}", employeePage.getTotal());
            return new PageResult<>(employeePage.getTotal(), list);
        }
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        requireAdmin();
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
    public Employee getEmployee(Long id) {
        Employee employee = employeeMapper.getById(id);
        if (!isAdmin()) {
            employee.setPhone(maskPhone(employee.getPhone()));
            employee.setIdNumber(maskIdNumber(employee.getIdNumber()));
        }
        log.debug("查询员工详情: id={}", id);
        return employee;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        Long empId = BaseContext.getCurrentId();
        log.info("修改密码: empId={}", empId);
        // getById 出于脱敏目的不查询 password 字段，此处必须单独查询密码密文
        String currentPassword = employeeMapper.getPasswordById(empId);
        if (currentPassword == null) {
            log.warn("修改密码失败: 账号不存在, empId={}", empId);
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        String oldPassword = toMD5(passwordEditDTO.getOldPassword());
        if (!oldPassword.equals(currentPassword)) {
            log.warn("修改密码失败: 旧密码错误, empId={}", empId);
            throw new PasswordEditFailedException(ErrorCode.PASSWORD_EDIT_FAILED, "旧密码错误");
        }
        String newPassword = toMD5(passwordEditDTO.getNewPassword());
        if (newPassword.equals(currentPassword)) {
            log.warn("修改密码失败: 新旧密码相同, empId={}", empId);
            throw new PasswordEditFailedException(ErrorCode.PASSWORD_EDIT_FAILED, "新旧密码不能相同");
        }
        employeeMapper.updateEmployee(Employee.builder()
                .id(empId)
                .password(newPassword)
                .build());
        log.info("修改密码成功: empId={}", empId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long empId, PasswordSetDTO passwordSetDTO) {
        requireAdmin();
        if (Objects.equals(BaseContext.getCurrentId(), empId)) {
            throw new UnauthorizedOperationException(ErrorCode.SECURITY_ERROR, "请通过修改密码接口修改本人密码");
        }
        Employee employee = employeeMapper.getById(empId);
        if (employee == null) {
            log.warn("修改员工密码失败: 账号不存在, empId={}", empId);
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        employeeMapper.updateEmployee(Employee.builder()
                .id(empId)
                .password(toMD5(passwordSetDTO.getNewPassword()))
                .build());
        log.warn("[密码修改审计] 操作人={}(id={}), 目标={}(username={}, id={})",
                BaseContext.getCurrentUserName(), BaseContext.getCurrentId(),
                employee.getName(), employee.getUsername(), empId);
    }

    @Override
    public void deleteEmployee(String ids) {
        requireAdmin();
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).toList();
        if (idList.contains(BaseContext.getCurrentId())) {
            throw new UnauthorizedOperationException(ErrorCode.SECURITY_ERROR, "不能删除当前登录用户");
        }
        log.info("删除员工: ids={}", idList);
        employeeMapper.deleteBatch(idList);
        log.info("删除员工成功: ids={}", idList);
    }

    /**
     * 判断当前登录人是否为管理员
     *
     * @return true-管理员，false-普通员工
     */
    private boolean isAdmin() {
        return Objects.equals(BaseContext.getCurrentRole(), JwtClaimsConstant.ADMIN);
    }

    /**
     * 要求当前登录人必须是管理员，否则抛出权限异常
     */
    private void requireAdmin() {
        if (!isAdmin()) {
            throw new UnauthorizedOperationException(ErrorCode.SECURITY_ERROR);
        }
    }

    /**
     * 计算字符串的 MD5 摘要
     *
     * @param raw 原始字符串
     * @return 十六进制 MD5 摘要
     */
    private String toMD5(String raw) {
        return DigestUtils.md5DigestAsHex(raw.getBytes());
    }

    /**
     * 手机号脱敏，非 11 位或为空时原样返回
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 身份证号脱敏，保留前 6 位与后 4 位，长度不足时原样返回
     *
     * @param idNumber 身份证号
     * @return 脱敏后的身份证号
     */
    private String maskIdNumber(String idNumber) {
        if (idNumber == null || idNumber.length() < 11) {
            return idNumber;
        }
        return idNumber.substring(0, 6) + "********" + idNumber.substring(idNumber.length() - 4);
    }
}