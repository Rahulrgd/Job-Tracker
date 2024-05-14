package com.rahul.job_tracker.Home;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.job_tracker.JobPost.JobPostServices;
import com.rahul.job_tracker.JwtAuthentication.JwtAuthenticationFilter;
import com.rahul.job_tracker.JwtAuthentication.JwtHelper;
import com.rahul.job_tracker.JwtAuthentication.JwtResponse;
import com.rahul.job_tracker.User.UserRestController;
import com.rahul.job_tracker.User.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

// @WebMvcTest(controllers = UserRestController.class)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
public class HomePageTest {

  @MockBean
  private JwtResponse jwtResponse;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserServices userServices;

  @MockBean
  private JobPostServices jobPostServices;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private HomePage homePage;

  //   @Mock
  //   private JwtHelper jwtHelper;

  //   @Mock
  //   private JwtAuthenticationFilter jwtAuthenticationFilter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @WithMockUser("user")
  void testWelcome() throws Exception {
   assertThat(homePage).isNotNull();

  }
}
