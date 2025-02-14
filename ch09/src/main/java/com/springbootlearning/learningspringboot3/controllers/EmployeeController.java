package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.entities.Employee;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  private final AtomicLong idGenerator;
  // Note: This is a simple in-memory database.
  private final Map<String, Employee> DATABASE =
      new LinkedHashMap<>() {
        {
          put("alice", new Employee(1L, "alice", "burglar"));
          put("bob", new Employee(2L, "bob", "ring-bearer"));
        }
      };

  public EmployeeController() {
    idGenerator = new AtomicLong(DATABASE.size());
  }

  @GetMapping
  Flux<Employee> employees() {
    return Flux.fromIterable(DATABASE.values());
  }

  @PostMapping
  public Mono<Employee> newEmployee(@RequestBody Mono<Employee> body) {
    return body.map(this::buildEmployeeToCreate).doOnNext(this::saveEmployee);
  }

  private Employee buildEmployeeToCreate(Employee newEmployee) {
    return new Employee(idGenerator.incrementAndGet(), newEmployee.name(), newEmployee.role());
  }

  private void saveEmployee(Employee employee) {
    DATABASE.put(employee.name(), employee);
  }
}
