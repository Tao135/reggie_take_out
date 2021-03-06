package com.czt.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czt.reggit.common.R;
import com.czt.reggit.pojo.Employee;
import com.czt.reggit.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());     //对密码进行md5加密

        //2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw);     //因为字段已经设置唯一，所以只需查询单个即可

        //3.如果没有查询到则返回登录失败结果
        if (emp == null){
            return R.error("登录失败，用户名不存在");
        }

        //4.密码对比，如果不一致返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败，密码错误");
        }

        //5.查看状态，如果为0，则为禁用状态，禁止使用
        if (emp.getStatus() == 0){
            return R.error("登录失败，账号已禁用");
        }

        //6.登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息为：{}",employee.toString());

        //根据用户名查询数据库中是否存在此数据
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw);     //因为字段已经设置唯一，所以只需查询单个即可

        //判断如果存在数据，此返回错误信息
        if (emp != null){
            return R.error("此用户名已存在");
        }

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //employee.setCreateTime(LocalDateTime.now());        //设置当前时间进行赋值
        //employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
        Long empId = (Long) request.getSession().getAttribute("employee");

        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" , page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        //添加过滤条件
        lqw.like(StringUtils.isNotEmpty(name),Employee::getName,name);       //当name不等于空时，执行后面的操作
        //添加排序条件
        lqw.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,lqw);         //无需传数据，因为mp会自动封装传值

        return R.success(pageInfo);
    }


    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
//        Long employeeId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(employeeId);
        employeeService.updateById(employee);
        return R.success("信息修改成功");
    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("查询出的员工信息:{}",employeeService.getById(id));
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("查询不到此员工信息");
    }
}
