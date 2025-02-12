package com.springbootlearning.learningspringboot3;

import com.springbootlearning.learningspringboot3.entities.Employee;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  private final AtomicLong idGenerator;
  private final Map<String, Employee> DATABASE =
      new HashMap<>() {
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
    return body.map(
            employee ->
                new Employee(idGenerator.incrementAndGet(), employee.name(), employee.role()))
        .doOnNext(employee -> DATABASE.put(employee.name(), employee));
  }
}
