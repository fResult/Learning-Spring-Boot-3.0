package com.springbootlearning.learningspringboot3;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

  private final VideoService videoService;

  public HomeController(VideoService videoService) {
    this.videoService = videoService;
  }

  record MyVideo(String name) {}

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("videos", videoService.getVideos());

    return "index";
  }

  @GetMapping("/react")
  public String react() {
    return "react";
  }

  @PostMapping("/new-video")
  public String newVideo(@ModelAttribute Video newVideo) {
    videoService.create(newVideo);
    return "redirect:/";
  }
}
