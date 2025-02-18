package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.entities.Employee;
import com.springbootlearning.learningspringboot3.repositories.EmployeeRepository;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
  public Mono<ResponseEntity<Employee>> byId(@PathVariable Long id) {
    return this.employeeRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  @PostMapping
  public Mono<ResponseEntity<Employee>> create(@RequestBody Mono<Employee> body) {
    return body.flatMap(this.employeeRepository::save).map(this::toCreatedResponseEntity);
  }

  private ResponseEntity<Employee> toCreatedResponseEntity(Employee createdResource) {
    return ResponseEntity.created(URI.create("/api/employees/" + createdResource.id()))
        .body(createdResource);
  }
}
