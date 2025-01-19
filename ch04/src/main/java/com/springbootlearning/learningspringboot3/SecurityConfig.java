package com.springbootlearning.learningspringboot3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    final var userDetailsManager = new InMemoryUserDetailsManager();
    userDetailsManager.createUser(
        User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build());

    userDetailsManager.createUser(
        User.builder()
            .username("admin")
            .password(passwordEncoder().encode("password"))
            .roles("ADMIN")
            .build());

    return userDetailsManager;
  }
}
