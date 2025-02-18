package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.entities.Employee;
import com.springbootlearning.learningspringboot3.repositories.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {
  private final EmployeeRepository employeeRepository;

  public HomeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @GetMapping("/")
  public Mono<Rendering> home() {
    return employeeRepository.findAll().collectList().map(this::toHomeRendering);
  }

  @PostMapping("/new-employee")
  Mono<String> createEmployee(@ModelAttribute("newEmployee") Mono<Employee> bodyMono) {
    return bodyMono.flatMap(employeeRepository::save).thenReturn("redirect:/");
  }

  private Rendering toHomeRendering(List<Employee> employees) {
    return Rendering.view("index")
        .modelAttribute("employees", employees)
        .modelAttribute("newEmployee", Employee.withoutId("", ""))
        .build();
  }
}
