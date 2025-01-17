package com.springbootlearning.learningspringboot3;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

  List<VideoEntity> findByNameContainsIgnoreCase(String partialName);

  List<VideoEntity> findByDescriptionContainsIgnoreCase(String partialDescription);

  List<VideoEntity> findByNameContainsOrDescriptionContainsAllIgnoreCase(
      String partialName, String partialDescription);
}
