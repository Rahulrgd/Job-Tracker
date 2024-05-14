package com.rahul.job_tracker.User;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.net.ssl.SSLEngineResult.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserServices userServices;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCountTotalUsers() {}

  @Test
  void testCreateUser() throws JsonProcessingException, Exception {
    UserDTO userDTO = UserDTO
      .builder()
      .email("rahi@gmail.com")
      .fullName("Rahi Turkar")
      .build();
    when(userServices.createUser(Mockito.any())).thenReturn(userDTO);
    mockMvc
      .perform(
        post("/sign-up/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(userDTO))
      )
      .andExpect(status().isOk());
  }

  @Test
  void testGetAllUsers() {}

  @Test
  void testGetUserDetails() {}
}
