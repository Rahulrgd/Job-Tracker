package com.rahul.job_tracker.JobPost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahul.job_tracker.Dashboard.TopPerformerDTO;
import com.rahul.job_tracker.JwtAuthentication.JwtHelper;
import com.rahul.job_tracker.Resume.ResumeServices;
import io.jsonwebtoken.JwtHandler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(controllers = JobPostRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class JobPostRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JobPostServices jobPostServices;

  //   @Autowired
  @MockBean
  private ResumeServices resumeServices;

  @MockBean
  private JwtHelper jwtHelper;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserDetailsService userDetailsService;

  private JobPost jobPost;
  private List<JobPostDTO> jobPosts;
  PageRequest pageable;
  private UUID jobPostId;
  private String jobPostIdString;
  private Integer pageNumber;

  @BeforeEach
  void setUp() {
    jobPost =
      JobPost
        .builder()
        .id(UUID.randomUUID())
        .companyName("abcd")
        .jobDate(LocalDate.now())
        .jobTitle("abcd")
        .jobLink("abcd")
        .status(JobStatusEnum.APPLIED)
        .build();
    jobPosts = new ArrayList<>();
    pageable = PageRequest.of(0, 7);

    jobPostId = UUID.randomUUID();
    jobPostIdString = jobPostId.toString();
    pageNumber = 0;
  }

  @Test
  @WithMockUser
  void testAddJob() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    when(jobPostServices.createJobPosts(jobPost))
      .thenReturn("Job post created successfully");
    mockMvc
      .perform(
        post("/v1/add-job")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(jobPost))
      )
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser
  void testAddJobWithJobId() throws Exception {
    when(jobPostServices.addJobWithJobId(jobPostId)).thenReturn(null);
    mockMvc
      .perform(
        post("/v1/add-job-with-job-id").param("jobPostId", jobPostIdString)
      )
      .andExpect(status().isOk());
  }

  @Test
  void testAllJobPosts() throws Exception {
    when(jobPostServices.allJobPosts(pageNumber)).thenReturn(jobPosts);
    mockMvc
      .perform(
        get("/v1/dashboard/all-jobs").param("pageNumber", pageNumber.toString())
      )
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testCountUserJobPosts() throws Exception {
    Integer countJobPosts = 1;
    when(jobPostServices.countUserJobPosts()).thenReturn(countJobPosts);
    mockMvc.perform(get("/v1/count-user-job-posts")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testDeleteUsersJobPost() throws Exception {
    mockMvc
      .perform(
        delete("/v1/delete-job-post").param("jobPostId", jobPostIdString)
      )
      .andExpect(status().isOk());
  }

  @Test
  void testRetrieveJobCountsPerDay() throws Exception {
    mockMvc
      .perform(
        get("/v1/dashboard/retrive-jobpost-count-per-day")
          .param("pageNumber", pageNumber.toString())
      )
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testRetrieveJobPostWithJobPostId() throws Exception {
    when(jobPostServices.retrieveUserJobPostWithId(jobPostId))
      .thenReturn(jobPost);
    mockMvc
      .perform(
        get("/v1/retrieve-job-post-with-user-id")
          .param("jobPostId", jobPostIdString)
      )
      .andExpect(status().isOk());
  }

  //   @Test
  @WithMockUser
  void testRetrieveJobPostsWithFiltersApplied() throws Exception {
    when(
      jobPostServices.retrieveJobsByFilters(
        Mockito.anyString(),
        Mockito.anyString(),
        Mockito.anyString(),
        Mockito.any(LocalDate.class),
        Mockito.any(JobStatusEnum.class)
      )
    )
      .thenReturn(jobPosts);
    mockMvc
      .perform(
        get("/v1/retrieve-job-posts-with-filters-applied")
          .param("jobTitle", "null")
          .param("companyName", "null")
          .param("jobDescription", "null")
          .param("jobDate", "null")
          .param("status", "null")
      )
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testRetrieveUserJobPosts() throws Exception {
    when(jobPostServices.retrieveUserJobPosts(pageNumber)).thenReturn(jobPosts);
    mockMvc
      .perform(
        get("/v1/retrieve-user-job-posts")
          .param("pageNumber", pageNumber.toString())
      )
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testRetrieveUserJobPostsContaingString() throws Exception {
    when(
      jobPostServices.retriveUserJobPostsContaingString(
        Mockito.anyString(),
        Mockito.anyInt()
      )
    )
      .thenReturn(jobPosts);
    mockMvc
      .perform(
        get("/v1/search-user-jobposts-containing-string")
          .param("string", "")
          .param("pageNumber", pageNumber.toString())
      )
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testRetrieveUsersPerDayJobPosts() throws Exception {
    when(jobPostServices.retrieveUsersPerDayJobPosts(Mockito.anyInt()))
      .thenReturn(new ArrayList<>());
    mockMvc
      .perform(
        get("/v1/retrive-users-per-day-jobposts")
          .param("pageNumber", pageNumber.toString())
      )
      .andExpect(status().isOk());
  }

  @Test
  void testRetriveJobPostsWithString() throws Exception {
    when(
      jobPostServices.retriveJobPostsContaingString(
        Mockito.anyString(),
        Mockito.anyInt()
      )
    )
      .thenReturn(jobPosts);
    mockMvc
      .perform(
        get("/v1/dashboard/search-jobposts-containing-strings")
          .param("string", "")
          .param("pageNumber", pageNumber.toString())
      )
      .andExpect(status().isOk());
  }

  //   @Test
  @WithMockUser
  void testSetResume() throws Exception {
    when(jobPostServices.setResume(jobPost, UUID.randomUUID()))
      .thenReturn(JobPostMapper.INSTANCE.toDTO(jobPost));
    mockMvc
      .perform(
        get("/v1/set-resume")
          .param("jobPost", jobPost.toString())
          .param("resumeID", UUID.randomUUID().toString())
      )
      .andExpect(status().isOk());
  }

  @Test
  void testTopPerformersOfTheDay() throws Exception {
    when(jobPostServices.retrieveTopPerformersOfTheDay())
      .thenReturn(new ArrayList<>());
    mockMvc
      .perform(
        get("/v1/dashboard/top-three-performer-of-the-day-with-their-job-count")
      )
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void testUpdateJobPost() throws Exception {
  when(jobPostServices.updateJobPost(jobPost)).thenReturn(Mockito.anyString());
    mockMvc
      .perform(
        put("/v1/update-job-post")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(jobPost))
      )
      .andExpect(status().isOk());
  }
}
