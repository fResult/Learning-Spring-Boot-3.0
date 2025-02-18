package com.springbootlearning.learningspringboot3.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("employees")
public record Employee(@Id Long id, String name, String role) {
  public Employee(String name, String role) {
    this(null, name, role);
  }
}
