package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.entities.Employee;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {
  private static final Map<String, Employee> DATABASE =
      new HashMap<>() {
        {
          put("alice", new Employee(1L, "alice", "burglar"));
          put("bob", new Employee(2L, "bob", "ring-bearer"));
        }
      };

  @GetMapping("/")
  public Mono<Rendering> index() {
    return Flux.fromIterable(DATABASE.values())
        .collectList()
        .map(employees -> Rendering.view("index").modelAttribute("employees", employees).build());
  }
}
