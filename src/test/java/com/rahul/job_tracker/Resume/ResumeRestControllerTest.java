package com.rahul.job_tracker.Resume;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.job_tracker.JwtAuthentication.JwtHelper;
import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeDTO;
import com.rahul.job_tracker.User.UserRepository;
import com.rahul.job_tracker.User.UserServices;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(controllers = ResumeRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ResumeRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ResumeServices resumeServices;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtHelper jwtHelper;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private ResumeRepository resumeRepository;

  private List<ResumeDTO> resumes;

  private MockMultipartFile file;
  private MultipartFile multipartFile;
  private Resume resume;
  private byte[] resumeFile;

  @BeforeEach
  void setUp() {
    resumes = new ArrayList<>();
    file =
      new MockMultipartFile(
        "file",
        "filename.txt",
        "text/plain",
        "This is the file content".getBytes()
      );
    resumeFile = new byte[1024];
    resume =
      Resume.builder().resumeFile(resumeFile).resumeName("Resume1").build();
  }

  @Test
  @WithMockUser
  void testCountUsersResume() throws Exception {
    when(resumeServices.getAllResume()).thenReturn(resumes);
    mockMvc.perform(get("/v1/all-resume")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  @DirtiesContext
  void testCreateResume() throws Exception {
    when(resumeServices.saveResume(null)).thenReturn(null);
    mockMvc
      .perform(MockMvcRequestBuilders.multipart("/v1/create-resume").file(file))
      .andExpect(status().isCreated());
  }

//   @Test
  @WithMockUser
  void testDeleteUserResume() throws Exception {
    when(resumeServices.deleteUserResume(resume.getId())).thenReturn(null);
    UUID resumeId = UUID.randomUUID();
    String resumeIdString = resumeId.toString();
    mockMvc.perform(get("/v1/delete-user-resume").param("resumeId", resumeIdString)).andExpect(status().isOk());
  }

//   @Test
  @WithMockUser
  void testDownloadUserResume() throws Exception {
    UUID resumeId = UUID.randomUUID();
    when(resumeServices.downloadUserResume(resumeId)).thenReturn(resumeFile);
    mockMvc.perform(get("/v1/download-user-resume")).andExpect(status().isOk());
  }

  @Test
  void testGetAllResume() throws Exception {
    when(resumeServices.getAllResume()).thenReturn(resumes);
    mockMvc.perform(get("/v1/all-resume")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testRetrieveUserResume() throws Exception {
    when(resumeServices.retrieveUserResumes()).thenReturn(resumes);
    mockMvc
      .perform(get("/v1/retrieve-user-resumes"))
      .andExpect(status().isOk());
  }
}
