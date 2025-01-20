package com.springbootlearning.learningspringboot3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CommandLineRunner initUsers(UserManagementRepository repository) {
    return (args) -> {
      repository.save(new UserAccount("user", "password", "USER"));
      repository.save(new UserAccount("admin", "password", "ADMIN"));
    };
  }

  @Bean
  public UserDetailsService userService(UserRepository userRepository) {
    return username -> userRepository.findByUsername(username).asUser(passwordEncoder());
  }


  }
}
