package com.howtodoinjava.demo.service;

import com.howtodoinjava.demo.exception.RecordNotFoundException;
import com.howtodoinjava.demo.model.EmployeeEntity;
import com.howtodoinjava.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * TODO :
 * The EmployeeService class provides methods to manage employee records.
 * It interacts with the EmployeeRepository to perform CRUD operations.
 */
public class EmployeeService {

    @Autowired
    EmployeeRepository repository;

    /**
     * TODO :
     * Retrieves all employee records.
     *
     * @return a list of all employees.
     */
    public List<EmployeeEntity> getAllEmployees() {
        return null;
    }

    /**
     * TODO :
     * Retrieves an employee record by ID.
     *
     * @param id the ID of the employee.
     * @return the employee entity.
     * @throws RecordNotFoundException if no employee record exists for the given ID.
     */
    public EmployeeEntity getEmployeeById(Long id) throws RecordNotFoundException {
        return null;
    }

    /**
     * TODO :
     * Creates or updates an employee record.
     *
     * @param entity the employee entity to create or update.
     * @return the created or updated employee entity.
     */
    public EmployeeEntity createOrUpdateEmployee(EmployeeEntity entity) {
        return null;
    }

    /**
     * TODO :
     * Deletes an employee record by ID.
     *
     * @param id the ID of the employee.
     * @throws RecordNotFoundException if no employee record exists for the given ID.
     */
    public void deleteEmployeeById(Long id) throws RecordNotFoundException {
        return;
    }
}