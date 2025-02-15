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
          put("Frodo Baggins", new Employee(1L, "Frodo Baggins", "Ring-bearer"));
          put("Samwise Gamgee", new Employee(2L, "Samwise Gamgee", "Gardener"));
          put("Bilbo Baggins", new Employee(3L, "Bilbo Baggins", "Burglar"));
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
