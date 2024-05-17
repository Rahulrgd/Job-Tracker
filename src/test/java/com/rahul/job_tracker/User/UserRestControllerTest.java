package com.rahul.job_tracker.User;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.job_tracker.Home.HomePage;
import com.rahul.job_tracker.JwtAuthentication.JwtHelper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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

  @MockBean
  private JwtHelper jwtHelper;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private UserRepository userRepository;

  @Test
  @WithMockUser(roles = "user")
  void testCountTotalUsers() throws Exception {
    when(userServices.countTotalUsers()).thenReturn(1l);
    mockMvc.perform(get("/v1/dashboard/count-total-users")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "user")
  @DirtiesContext
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
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "user")
  void testGetAllUsers() throws Exception {
    List<UserDTO> users = new ArrayList<>();
    when(userServices.getAllUsers(1)).thenReturn(users);
    mockMvc
      .perform(get("/all-users").param("pageNumber", "1"))
      .andExpect(status().isOk());
  }

  @Test
  void testGetUserDetails() throws Exception {
    UserDTO userDTO = new UserDTO();
    when(userServices.getUserDetails()).thenReturn(userDTO);
    mockMvc.perform(get("/v1/user-detail")).andExpect(status().isOk());
  }
}
