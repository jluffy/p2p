package com.xmg.p2p.api.test;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * springBoot restful开发测试控制器
 */
@RestController//restController表示Controller+responsbody
public class RestTestController {
    /**
     * 获取所有员工的信息
     * @return返回json数据
     */
    @RequestMapping(value = "/emps/list", method = RequestMethod.GET)
    public List<Employee> listEmps() {
        List<Employee> emps = new ArrayList<Employee>();
        Employee employee = new Employee(1L, "lucy");
        Employee employee1 = new Employee(2L, "lily");
        emps.add(employee);
        emps.add(employee1);
        return emps;
    }

    /**
     * 获取某个员工的信息
     */
    @RequestMapping(value = "/emps/{id}",method = RequestMethod.GET)
    public Employee getEmp(@PathVariable("id") Long id){

        return new Employee(id,"luffy");
    }

}
