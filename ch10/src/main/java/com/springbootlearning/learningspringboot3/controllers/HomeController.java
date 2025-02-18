package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.repositories.EmployeeRepository;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
  private final EmployeeRepository employeeRepository;

  public HomeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }
}
