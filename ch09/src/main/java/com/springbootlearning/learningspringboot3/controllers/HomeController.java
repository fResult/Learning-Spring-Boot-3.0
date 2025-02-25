package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.entities.Employee;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {
  private AtomicLong idGenerator = new AtomicLong(DATABASE.size());
  private static final Map<String, Employee> DATABASE =
      new HashMap<>() {
        {
          put("Frodo Baggins", new Employee(1L, "Frodo Baggins", "Ring-bearer"));
          put("Samwise Gamgee", new Employee(2L, "Samwise Gamgee", "Gardener"));
          put("Bilbo Baggins", new Employee(3L, "Bilbo Baggins", "Burglar"));
        }
      };

  @GetMapping("/")
  public Mono<Rendering> index() {
    return Flux.fromIterable(DATABASE.values())
        .collectList()
        .map(
            employees ->
                Rendering.view("index")
                    .modelAttribute("employees", employees)
                    .modelAttribute("newEmployee", new Employee(null, "", ""))
                    .build());
  }

  @PostMapping("/new-employee")
  Mono<String> newEmployee(@ModelAttribute Mono<Employee> newEmployee) {
    return newEmployee.doOnNext(this::saveEmployee).thenReturn("redirect:/");
  }

  private void saveEmployee(Employee employee) {
    DATABASE.put(
        employee.name(),
        new Employee(idGenerator.incrementAndGet(), employee.name(), employee.role()));
  }
}
