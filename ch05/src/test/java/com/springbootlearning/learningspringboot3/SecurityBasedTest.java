package com.springbootlearning.learningspringboot3;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HomeController.class)
class SecurityBasedTest {
  @Autowired private MockMvc mockMvc;

  @MockBean VideoService videoService;

  @Test
  void unauthenticatedUserShouldNotAccessHomePage() throws Exception {
    mockMvc.perform(get("/").with(csrf())).andExpect(status().isUnauthorized());
  }
}
