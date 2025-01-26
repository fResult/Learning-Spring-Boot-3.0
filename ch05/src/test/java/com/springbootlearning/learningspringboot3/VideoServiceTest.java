package com.springbootlearning.learningspringboot3;


import org.junit.jupiter.api.BeforeEach;
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
}
