package com.springbootlearning.learningspringboot3;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HomeController.class)
class SecurityBasedTest {
  @Autowired private MockMvc mockMvc;

  @MockBean VideoService videoService;

  @Test
  void unauthenticatedUserShouldNotAccessHomePage() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "alice", roles = "USER")
  void authUserShouldAccessHomePage() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void authAdminShouldAccessHomePage() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }
}
