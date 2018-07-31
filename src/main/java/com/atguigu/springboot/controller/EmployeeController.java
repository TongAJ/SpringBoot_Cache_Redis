package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.Employee;
import com.atguigu.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService service;

    @GetMapping("/emp/{id}")
    public Employee getEmployee(@PathVariable("id")Integer id){
        Employee employee = service.getEmployee(id);
        return employee;
    }

    @GetMapping("/emp")
    public Employee updateEmployee(Employee employee){
        Employee emp = service.updateEmployee(employee);
        return emp;
    }

   @GetMapping("/delemp/{id}")
    public void deleteEmployee(@PathVariable("id")Integer id){
        service.deleteEmployee(id);
        int i =10/0;
    }
}
