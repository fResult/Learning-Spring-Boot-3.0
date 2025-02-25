package com.springbootlearning.learningspringboot3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CommandLineRunner initUsers(UserManagementRepository repository) {
    return args -> {
      repository.save(new UserAccount("user", "password", "ROLE_USER"));
      repository.save(new UserAccount("admin", "password", "ROLE_ADMIN"));
    };
  }

  @Bean
  public UserDetailsService userService(UserRepository userRepository) {
    return username -> userRepository.findByUsername(username).asUser(passwordEncoder());
  }

  @Bean
  SecurityFilterChain configureSecurity(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeHttpRequests()
        .requestMatchers("/login")
        .permitAll()
        .requestMatchers("/", "/search")
        .authenticated()
        .requestMatchers(HttpMethod.GET, "/api/**")
        .authenticated()
        .requestMatchers(HttpMethod.POST, "/new-video", "/api/**")
        .hasRole("ADMIN")
        .anyRequest()
        .denyAll()
        .and()
        .formLogin()
        .and()
        .httpBasic()
        .and()
        .csrf()
        .disable();

    return httpSecurity.build();
  }
}
