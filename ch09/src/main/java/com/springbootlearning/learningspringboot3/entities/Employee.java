package com.springbootlearning.learningspringboot3.entities;

public record Employee(Long id, String name, String role) {
  // NOTE: this constructor leads to the runtime error "No primary or single unique constructor found for `Employee` in Thymeleaf template"
  // public Employee(String name, String role) {
  //   this(null, name, role);
  // }
}
