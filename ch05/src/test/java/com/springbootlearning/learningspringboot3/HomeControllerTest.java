package com.springbootlearning.learningspringboot3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private VideoService videoService;

  @Test
  @WithMockUser
  void indexPageHasSeveralHtmlForms() throws Exception {
    final var html =
        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Username: user")))
            .andExpect(content().string(containsString("Authorities: [ROLE_USER]")))
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(html)
        .contains(
            "<form action=\"/logout\"", "<form action=\"/search\"", "<form action=\"/new-video\"");
  }

  @Test
  @WithMockUser
  void postNewVideoShouldWork() throws Exception {
    mockMvc
        .perform(
            post("/new-video")
                .param("name", "new video")
                .param("description", "description")
                .with(csrf()))
        .andExpect(redirectedUrl("/"));

    verify(videoService).create(new NewVideo("new video", "description"), "user");
  }

  @Test
  @WithMockUser
  void searchVideosShouldWork() throws Exception {
    final var keyword = "my video";
    mockMvc
        .perform(
            post("/search")
                .param("value", keyword)
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(String.format("Searching: %s", keyword))));

    verify(videoService).search(new Search(keyword));
  }

  @Test
  @WithMockUser
  void deleteVideoShouldWork() throws Exception {
    var videoId = 123L;
    mockMvc.perform(post("/delete/videos/{videoId}", videoId).with(csrf())).andExpect(redirectedUrl("/"));

    verify(videoService).delete(videoId);
  }
}
