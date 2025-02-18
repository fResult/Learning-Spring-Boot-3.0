package com.springbootlearning.learningspringboot3.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("employees")
public record Employee(@Id Long id, String name, String role) {
  public static Employee withoutId(String name, String role) {
    return new Employee(null, name, role);
  }
}
