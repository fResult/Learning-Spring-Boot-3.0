package com.springbootlearning.learningspringboot3;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = VideoRepositoryTestContainersTest.DataSourceInitializer.class)
class VideoRepositoryTestContainersTest {
  @Autowired private VideoRepository videoRepository;

  @Container
  static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:16-alpine").withUsername("postgres");

  @BeforeAll
  static void setUp() {
    POSTGRESQL_CONTAINER.start();
  }

  @AfterAll
  static void tearDown() {
    POSTGRESQL_CONTAINER.stop();
  }

  @BeforeEach
  void setUpEach() {
    videoRepository.saveAll( //
        List.of( //
            new VideoEntity( //
                "alice", //
                "Need HELP with your SPRING BOOT 3 App?", //
                "SPRING BOOT 3 will only speed things up."),
            new VideoEntity(
                "alice", //
                "Don't do THIS to your own CODE!", //
                "As a pro developer, never ever EVER do this to your code."),
            new VideoEntity(
                "bob", //
                "SECRETS to fix BROKEN CODE!", //
                "Discover ways to not only debug your code")));
  }

  @Test
  void findAllShouldProduceAllVideos() {
    final var videos = videoRepository.findAll();
    assertThat(videos).hasSize(3);
  }

  @Test
  void findByNameShouldReturnOne() {
    final var videos = videoRepository.findByNameContainsIgnoreCase("Spring BOOT 3");
    assertThat(videos).hasSize(1);
  }

  @Test
  void findByNameOrDescriptionShouldReturnTwo() {
    final var videos =
        videoRepository.findByNameContainsOrDescriptionContainsAllIgnoreCase("cOdE", "YOUR code");
    assertThat(videos).hasSize(2);
  }

  static class DataSourceInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      System.out.println("initializeeeee");
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
          applicationContext,
          "spring.datasource.url=" + POSTGRESQL_CONTAINER.getJdbcUrl(),
          "spring.datasource.username=" + POSTGRESQL_CONTAINER.getUsername(),
          "spring.datasource.password=" + POSTGRESQL_CONTAINER.getPassword(),
          "spring.jpa.hibernate.ddl-auto=create-drop");
    }
  }
}
