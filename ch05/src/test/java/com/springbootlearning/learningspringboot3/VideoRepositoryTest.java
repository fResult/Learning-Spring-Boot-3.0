package com.springbootlearning.learningspringboot3;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class VideoRepositoryTest {
  @Autowired VideoRepository videoRepository;

  @BeforeEach
  void setUp() {
    final var video1 =
        new VideoEntity(
            "user",
            "Need HELP with your SPRING BOOT 3 App?",
            "SPRING BOOT 3 will only speed things up.");
    final var video2 =
        new VideoEntity(
            "user",
            "Don't do THIS to your own CODE!",
            "As a pro developer, never ever EVER do this to your code.");
    final var video3 =
        new VideoEntity(
            "user", "SECRETS to fix BROKEN CODE!", "Discover ways to not only debug your code");
    videoRepository.saveAll(List.of(video1, video2, video3));
  }

  @Test
  void findAllByUsernameShouldReturnAllVideosForUser() {
    // When
    final var videos = videoRepository.findAll();

    // Then
    assertThat(videos).hasSize(3);
  }

  @Test
  void findByNameShouldRetrieveOneEntry() {
    final var videos = videoRepository.findByNameContainsIgnoreCase("Spring BOOT 3");

    assertThat(videos).hasSize(1);
    assertThat(videos)
        .extracting(VideoEntity::getName)
        .containsExactlyInAnyOrder("Need HELP with your SPRING BOOT 3 App?");
  }

  @Test
  void findByNameOrDescriptionShouldFindTwo() {
    final var videos =
        videoRepository.findByNameContainsOrDescriptionContainsAllIgnoreCase("cOdE", "YOUR code");

    assertThat(videos).hasSize(2);
    assertThat(videos)
        .extracting(VideoEntity::getDescription)
        .contains(
            "Discover ways to not only debug your code",
            "As a pro developer, never ever EVER do this to your code.");
  }
}
