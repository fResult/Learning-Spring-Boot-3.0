package com.springbootlearning.learningspringboot3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
class HomeControllerTest {
  @Autowired MockMvc mockMvc;

  @MockBean VideoService videoService;

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
        .contains("<form action=\"/logout\"", "<form action=\"/search\"", "<form action=\"/new-video\"");
  }
}
