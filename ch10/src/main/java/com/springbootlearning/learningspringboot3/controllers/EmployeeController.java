package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.entities.Employee;
import com.springbootlearning.learningspringboot3.repositories.EmployeeRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  private final EmployeeRepository employeeRepository;

  EmployeeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @GetMapping
  public Mono<ResponseEntity<List<Employee>>> all() {
    return this.employeeRepository.findAll().collectList().map(ResponseEntity::ok);
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Employee>> byId(Long id) {
    return this.employeeRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }
}
