package com.springbootlearning.learningspringboot3.controllers;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

import com.springbootlearning.learningspringboot3.entities.Employee;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hypermedia/employees")
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class HypermediaController {
  private final Map<Long, Employee> DATABASE =
      new HashMap<>() {
        {
          put(1L, new Employee(1L, "Frodo Baggins", "Ring-bearer"));
          put(2L, new Employee(2L, "Samwise Gamgee", "Gardener"));
          put(3L, new Employee(3L, "Bilbo Baggins", "Burglar"));
        }
      };
  private AtomicLong idGenerator = new AtomicLong(DATABASE.size());

  @GetMapping
  public Mono<CollectionModel<EntityModel<Employee>>> allEmployees() {
    final var selfLinkMono =
        linkTo(methodOn(this.getClass()).allEmployees()).withSelfRel().toMono();

    return selfLinkMono.flatMap(
        selfLink -> Flux.fromIterable(DATABASE.keySet())
            .flatMap(this::employeeById)
            .collectList()
            .map(employeeEntityModels -> CollectionModel.of(employeeEntityModels, selfLink)));
  }

  @GetMapping("/{id}")
  public Mono<EntityModel<Employee>> employeeById(@PathVariable Long id) {
    final var selfLinkMono =
        linkTo(methodOn(this.getClass()).employeeById(id)).withSelfRel().toMono();
    final var aggregateRootLinkMono =
        linkTo(methodOn(this.getClass()).allEmployees()).withRel("employees").toMono();

    return Mono.zip(selfLinkMono, aggregateRootLinkMono)
        .map(
            linksTuple ->
                EntityModel.of(
                    Optional.of(DATABASE.get(id))
                        .orElseThrow(
                            () ->
                                new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    String.format("Employee with ID %d not found", id))),
                    linksTuple.getT1(),
                    linksTuple.getT2()));
  }
}
