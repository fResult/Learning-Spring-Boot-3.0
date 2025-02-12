package com.springbootlearning.learningspringboot3.entities;

import java.util.function.Function;

public record Employee(Long id, String name, String role) {
  public Employee(String name, String role) {
    this(null, name, role);
  }

  public Employee {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
  }
}
