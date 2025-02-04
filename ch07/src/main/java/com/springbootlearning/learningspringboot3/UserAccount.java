package com.springbootlearning.learningspringboot3;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class UserAccount {
  @Id @GeneratedValue //
  private Long id;
  private String username;
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<GrantedAuthority> authorities;

  protected UserAccount() {}

  public UserAccount(String username, String password, String... authorities) {
    this.username = username;
    this.password = password;
    this.authorities =
        Arrays.stream(authorities)
            .map((Function<String, GrantedAuthority>) SimpleGrantedAuthority::new)
            .toList();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    return User.withUsername(username)
        .password(passwordEncoder.encode(password))
        .authorities(authorities)
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserAccount user = (UserAccount) o;
    return Objects.equals(id, user.id)
        && Objects.equals(username, user.username)
        && Objects.equals(password, user.password)
        && Objects.equals(authorities, user.authorities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, authorities);
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", authorities="
        + authorities
        + '}';
  }
}
