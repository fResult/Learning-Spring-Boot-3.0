package com.springbootlearning.learningspringboot3.repositories;

import com.springbootlearning.learningspringboot3.entities.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {}
