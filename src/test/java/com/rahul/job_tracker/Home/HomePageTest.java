package com.rahul.job_tracker.Home;

import com.rahul.job_tracker.JwtAuthentication.JwtHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = HomePage.class)
@ExtendWith(MockitoExtension.class)
public class HomePageTest {

  @MockBean
  private JwtHelper jwtHelper;

  @MockBean
  private UserDetailsService userDetailsService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(roles = "user")
  void testWelcome() throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get("/")
          .with(SecurityMockMvcRequestPostProcessors.jwt())
      )
      .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
