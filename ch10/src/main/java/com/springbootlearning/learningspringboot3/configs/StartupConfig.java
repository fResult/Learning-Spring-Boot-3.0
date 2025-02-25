package com.springbootlearning.learningspringboot3.configs;

import com.springbootlearning.learningspringboot3.entities.Employee;
import java.util.function.Consumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

@Configuration
public class StartupConfig {
  @Bean
  CommandLineRunner initEmployees(R2dbcEntityTemplate template) {
    return args -> {
      template
          .getDatabaseClient()
          .sql(
              "CREATE TABLE IF NOT EXISTS employees (id IDENTITY NOT NULL PRIMARY KEY, name VARCHAR(255), role VARCHAR(255))")
          .fetch()
          .rowsUpdated()
          .as(StepVerifier::create)
          .expectNextCount(1)
          .verifyComplete();

      final var insertEmployeeToTable = insertEmployee(template);
      insertEmployeeToTable.accept(Employee.withoutId("Frodo", "ring bearer"));
      insertEmployeeToTable.accept(Employee.withoutId("Bilbo", "burglar"));
      insertEmployeeToTable.accept(Employee.withoutId("Gandalf", "wizard"));
    };
  }

  private Consumer<Employee> insertEmployee(R2dbcEntityTemplate template) {
    return employee ->
        template
            .insert(Employee.class)
            .using(employee)
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
  }
}
