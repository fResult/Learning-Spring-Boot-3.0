package com.springbootlearning.learningspringboot3.controllers;

import com.springbootlearning.learningspringboot3.dtos.EmployeeUpdateRequest;
import com.springbootlearning.learningspringboot3.entities.Employee;
import com.springbootlearning.learningspringboot3.repositories.EmployeeRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

  @PatchMapping("/{id}")
  public Mono<ResponseEntity<Employee>> update(
      @PathVariable Long id, @RequestBody Mono<EmployeeUpdateRequest> bodyMono) {

    return employeeRepository
        .findById(id)
        .flatMap(existingEmployee -> bodyMono.map(this.toEmployeeToUpdate(existingEmployee)))
        .flatMap(employeeRepository::save)
        .map(ResponseEntity::ok)
        .switchIfEmpty(respondNotFoundMono());
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
    return employeeRepository
        .deleteById(id)
        .map(deleted -> ResponseEntity.noContent().<Void>build())
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  private ResponseEntity<Employee> toCreatedResponseEntity(Employee createdResource) {
    return ResponseEntity.created(URI.create("/api/employees/" + createdResource.id()))
        .body(createdResource);
  }

  private Function<EmployeeUpdateRequest, Employee> toEmployeeToUpdate(Employee existingEmployee) {
    return body ->
        new Employee(
            existingEmployee.id(),
            Optional.ofNullable(body.name()).orElse(existingEmployee.name()),
            Optional.ofNullable(body.role()).orElse(existingEmployee.role()));
  }

  private Mono<ResponseEntity<Employee>> respondNotFoundMono() {
    return Mono.just(ResponseEntity.notFound().build());
  }
}
