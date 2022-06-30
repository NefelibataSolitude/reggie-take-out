/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/27 20:57
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.tecoy.reggie.pojo.entity.Employee;
import net.tecoy.reggie.mapper.EmployeeMapper;
import net.tecoy.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
