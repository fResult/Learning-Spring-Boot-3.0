package com.springbootlearning.learningspringboot3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class YouTubeConfig {
  static final String YOUTUBE_V3_API = "https://www.googleapis.com/youtube/v3";

  @Bean
  public WebClient webClient(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
    final var oAuth2 =
        new ServletOAuth2AuthorizedClientExchangeFilterFunction(oAuth2AuthorizedClientManager);

    oAuth2.setDefaultClientRegistrationId("google");

    return WebClient.builder() //
        .baseUrl(YOUTUBE_V3_API)
        .apply(oAuth2.oauth2Configuration())
        .build();
  }

  @Bean
  HttpServiceProxyFactory proxyFactory(WebClient webClient) {
    return HttpServiceProxyFactory.builder().clientAdapter(WebClientAdapter.forClient(webClient)).build();
  }

  @Bean
  YouTube youTubeClient(HttpServiceProxyFactory proxyFactory) {
    return proxyFactory.createClient(YouTube.class);
  }
}
