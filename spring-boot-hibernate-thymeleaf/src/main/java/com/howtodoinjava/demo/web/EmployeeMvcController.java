package com.howtodoinjava.demo.web;

import com.howtodoinjava.demo.exception.RecordNotFoundException;
import com.howtodoinjava.demo.model.EmployeeEntity;
import com.howtodoinjava.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

/**
 * TODO:
 * The EmployeeMvcController class handles HTTP requests for managing employee records.
 * It provides methods to list all employees, edit an employee by ID, delete an employee by ID,
 * and create or update an employee record.
 */

public class EmployeeMvcController {
    @Autowired
    EmployeeService service;

    public String getAllEmployees(Model model) {
        return "";
    }

    public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id)
            throws RecordNotFoundException {

        return "add-edit-employee";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteEmployeeById(Model model, @PathVariable("id") Long id)
            throws RecordNotFoundException {

        return "redirect:/";
    }

    @RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
    public String createOrUpdateEmployee(EmployeeEntity employee) {
        return "redirect:/";
    }
}
