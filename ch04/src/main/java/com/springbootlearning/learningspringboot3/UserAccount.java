package com.springbootlearning.learningspringboot3;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class UserAccount {
  @Id @GeneratedValue private Long id;
  private String username;
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<GrantedAuthority> authorities;

  public UserAccount() {}

  public UserAccount(String username, String password, String... authorities) {
    this.username = username;
    this.password = password;
    this.authorities =
        Arrays.stream(authorities).map(this::toGrantedAuthorityWithRolePrefix).toList();
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public List<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public UserDetails asUser(PasswordEncoder passwordEncoder) {
    return User.builder()
        .username(username)
        .password(passwordEncoder.encode(password))
        .authorities(authorities)
        .build();
  }

  @Override
  public String toString() {
    return "UserAccount{id="
        + id
        + ", username="
        + username
        + ", password="
        + password
        + ", authorities="
        + authorities
        + "}";
  }

  private GrantedAuthority toGrantedAuthorityWithRolePrefix(String role) {
    return new SimpleGrantedAuthority("ROLE_" + role);
  }
}
