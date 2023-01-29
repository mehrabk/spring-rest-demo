package com.mehrab.springdemo.controller;

import com.mehrab.springdemo.model.Employees;
import com.mehrab.springdemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/getAll")
    public List<Employees> getAll() {
        return employeeRepository.findAll();
    }
}
