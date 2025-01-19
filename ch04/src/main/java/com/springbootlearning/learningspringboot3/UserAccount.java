package com.springbootlearning.learningspringboot3;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class UserAccount {
  @Id @GeneratedValue private Long id;
  private String username;
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<GrantedAuthority> authorities;

  public UserAccount(String username, String password, String... authorities) {
    this.username = username;
    this.password = password;
    this.authorities =
        Arrays.stream(authorities)
            .map((Function<String, GrantedAuthority>) SimpleGrantedAuthority::new)
            .toList();
  }

  public UserDetails asUser() {
    return User.withDefaultPasswordEncoder()
        .username(username)
        .password(password)
        .authorities(authorities)
        .build();
  }
}
