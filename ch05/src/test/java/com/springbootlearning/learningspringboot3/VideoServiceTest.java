package com.springbootlearning.learningspringboot3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {
  private VideoService videoService;

  @Mock VideoRepository videoRepository;

  @BeforeEach
  void setUp() {
    this.videoService = new VideoService(videoRepository);
  }

  @Test
  void getVideosShouldReturnAll() {
    // Given
    final var video1 = new VideoEntity("alice", "video 1", "description");
    final var video2 = new VideoEntity("alice", "video 2", "descriptionnnn");
    when(videoRepository.findAll()).thenReturn(List.of(video1, video2));

    // When
    final var videos = videoService.getVideos();

    // Then
    assertThat(videos).containsExactly(video1, video2);
  }

  @Test
  void creatingNewVideoShouldReturnSameDataWithId() {
    // Given
    final var newVideo = new NewVideo("video 1", "description");
    final var videoToCreate = new VideoEntity("alice", "video 1", "description");
    videoToCreate.setId(1L);
    given(videoRepository.saveAndFlush(any(VideoEntity.class))).willReturn(videoToCreate);

    // When
    final var actualCreatedVideo = videoService.create(newVideo, "alice");

    // Then
    assertThat(actualCreatedVideo.getName()).isEqualTo("video 1");
    assertThat(actualCreatedVideo.getUsername()).isEqualTo("alice");
    assertThat(actualCreatedVideo.getDescription()).isEqualTo("description");
  }

  @Test
  void deleteVideoShouldWork() {
    // Given
    final var videoId = 1L;
    final var videoToDelete = new VideoEntity("alice", "video 1", "description");
    videoToDelete.setId(videoId);
    when(videoRepository.findById(1L)).thenReturn(Optional.of(videoToDelete));

    // When
    videoService.delete(videoId);

    // Then
    verify(videoRepository).findById(videoId);
    verify(videoRepository).delete(videoToDelete);
  }
}
