package com.springbootlearning.learningspringboot3.controllers;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

import com.springbootlearning.learningspringboot3.entities.Employee;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

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

    return selfLinkMono.flatMap(this::toEmployeeEntityModelCollectionMono);
  }

  @GetMapping("/{id}")
  public Mono<EntityModel<Employee>> employeeById(@PathVariable Long id) {
    final var selfLinkMono =
        linkTo(methodOn(this.getClass()).employeeById(id)).withSelfRel().toMono();
    final var aggregateRootLinkMono =
        linkTo(methodOn(this.getClass()).allEmployees()).withRel("employees").toMono();

    return Mono.zip(selfLinkMono, aggregateRootLinkMono).flatMap(toEmployeeEntityModelMono(id));
  }

  private Mono<CollectionModel<EntityModel<Employee>>> toEmployeeEntityModelCollectionMono(
      Link link) {

    final Function<List<EntityModel<Employee>>, CollectionModel<EntityModel<Employee>>>
        toEntityModelCollection = entityModels -> CollectionModel.of(entityModels, link);

    return Flux.fromIterable(DATABASE.keySet())
        .flatMap(this::employeeById)
        .collectList()
        .map(toEntityModelCollection);
  }

  private Function<Tuple2<Link, Link>, Mono<EntityModel<Employee>>> toEmployeeEntityModelMono(
      Long id) {

    final var notfoundErrorMono =
        Mono.<Employee>error(
            new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Employee with ID %d not found", id)));

    return linksTuple -> {
      final var employeeMono = Mono.just(DATABASE.get(id)).switchIfEmpty(notfoundErrorMono);

      final Function<Employee, EntityModel<Employee>> toEntityModel =
          employee -> EntityModel.of(employee, linksTuple.getT1(), linksTuple.getT2());

      return employeeMono.map(toEntityModel);
    };
  }
}
