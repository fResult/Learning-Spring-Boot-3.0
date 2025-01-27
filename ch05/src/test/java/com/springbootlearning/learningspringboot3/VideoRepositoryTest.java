package com.springbootlearning.learningspringboot3;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class VideoRepositoryTest {
  @Autowired VideoRepository videoRepository;

  @BeforeEach
  void setUp() {
    final var video1 = new VideoEntity("user", "video1", "video1");
    final var video2 = new VideoEntity("user", "video2", "video2");
    final var video3 = new VideoEntity("user", "video3", "video3");
    videoRepository.saveAll(List.of(video1, video2, video3));
  }
}
