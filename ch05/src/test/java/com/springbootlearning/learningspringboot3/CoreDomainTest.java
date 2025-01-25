package com.springbootlearning.learningspringboot3;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CoreDomainTest {
  @Test
  void newVideoShouldHaveNoId() {
    final var video = new VideoEntity("Alice", "title", "description");
    assertThat(video.getId()).isNull();
    assertThat(video.getUsername()).isEqualTo("Alice");
    assertThat(video.getName()).isEqualTo("title");
    assertThat(video.getDescription()).isEqualTo("description");
  }

  @Test
  void toStringShouldAlsoBeTested() {
    final var video = new VideoEntity("Alice", "title", "description");
    assertThat(video.toString()).isEqualTo("VideoEntity{id=null, username='Alice', name='title', description='description'}");
  }

  @Test
  void settersShouldMutateState() {
    final var video = new VideoEntity("Alice", "title", "description");
    video.setId(99L);
    video.setUsername("Bob");
    video.setName("new title");
    video.setDescription("new description");
    assertThat(video.getId()).isEqualTo(99L);
    assertThat(video.getUsername()).isEqualTo("Bob");
    assertThat(video.getName()).isEqualTo("new title");
    assertThat(video.getDescription()).isEqualTo("new description");
  }
}
