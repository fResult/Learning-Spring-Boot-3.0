package com.springbootlearning.learningspringboot3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class SecurityConfig {
  @Bean
  public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
      ClientRegistrationRepository clientRegRepository,
      OAuth2AuthorizedClientRepository authClientRepository) {

    final var clientManager =
        new DefaultOAuth2AuthorizedClientManager(clientRegRepository, authClientRepository);
    final var clientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken()
            .clientCredentials()
            .password()
            .build();

    clientManager.setAuthorizedClientProvider(clientProvider);

    return clientManager;
  }
}
