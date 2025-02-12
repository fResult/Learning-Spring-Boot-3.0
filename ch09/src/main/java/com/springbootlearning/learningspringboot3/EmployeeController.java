package com.springbootlearning.learningspringboot3;

import com.springbootlearning.learningspringboot3.entities.Employee;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  private final Map<String, Employee> DATABASE =
      new HashMap<>() {
        {
          put("alice", new Employee(1L, "alice", "burglar"));
          put("bob", new Employee(2L, "bob", "ring-bearer"));
        }
      };

  @GetMapping
  Flux<Employee> employees() {
    return Flux.fromIterable(DATABASE.values());
  }
}
