package com.rahul.job_tracker.Resume;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeDTO;
import com.rahul.job_tracker.User.User;
import com.rahul.job_tracker.User.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ResumeServicesTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private Authentication authentication;

  @InjectMocks
  private ResumeServicesImpl resumeServices;

  @Mock
  private ResumeRepository resumeRepository;

  private User user;
  private Resume resume;
  private byte[] resumeFile;
  private MultipartFile multipartFile;

  @BeforeEach
  void setUp() {
    user =
      User
        .builder()
        .email("rahul@gmail.com")
        .fullName("Rahul Gupta")
        .password("12345678")
        .build();
    userRepository.save(user);
    resumeFile = new byte[1024];
    resume =
      Resume.builder().resumeFile(resumeFile).resumeName("Resume1").build();

  }

  @Test
  @WithMockUser
  void testCountUserResumes() {
    Authentication authentication = (Authentication) SecurityContextHolder
      .getContext()
      .getAuthentication();
    if (authentication != null) {
      when(
        (
          (org.springframework.security.core.Authentication) authentication
        ).getPrincipal()
      )
        .thenReturn(user);
      when(resumeRepository.countByUser(user)).thenReturn(1);
      assertThat(resumeServices.countUserResumes()).isNotNull();
    } else {}
  }

  @Test
  @WithMockUser
  void testDeleteUserResume() {
    UUID resumeId = new UUID(0, 0);
    assertThat(resumeServices.deleteUserResume(resumeId)).isNotNull();
  }

  //   @Test
  @WithMockUser
  void testDownloadUserResume() throws NoSuchElementException {
    UUID resumeId = resume.getId();
    System.out.println("Resume ID: " + resumeId);
    Optional<byte[]> optionalResume = Optional.ofNullable(
      resumeServices.downloadUserResume(resumeId)
    );
    System.out.println(
      "Optional contains value: " + optionalResume.isPresent()
    );
    // Check if the Optional contains a value
    if (optionalResume.isPresent()) {
      assertThat(optionalResume.get()).isNotNull();
    } else {}
  }

  @Test
  void testGetAllResume() {
    List<ResumeDTO> resumeDTOs = new ArrayList<>();
    when(
      resumeRepository
        .findAll()
        .stream()
        .map(resume -> ResumeMapper.INSTANCE.toDTO(resume))
        .collect(Collectors.toList())
    )
      .thenReturn(resumeDTOs);
    assertThat(resumeServices.getAllResume()).isNotNull();
  }

  @Test
  void testGetUser() {}

  @Test
  @WithMockUser
  void testRetrieveUserResumes() {
    List<Resume> resumes = new ArrayList<>();
    Authentication authentication = (Authentication) SecurityContextHolder
      .getContext()
      .getAuthentication();
    if (authentication != null) {
      when(
        (
          (org.springframework.security.core.Authentication) authentication
        ).getPrincipal()
      )
        .thenReturn(user);
      when(resumeRepository.findByUser(user)).thenReturn(resumes);
      assertThat(resumeServices.retrieveUserResumes()).isNotNull();
    } else {}
  }

  @Test
  void testSaveResume() {
    Authentication authentication = (Authentication) 
    SecurityContextHolder
      .getContext()
      .getAuthentication();
    Resume resume = Resume.builder().resumeFile(resumeFile).build();
    if (authentication != null) {
      when(
        (
          (org.springframework.security.core.Authentication) authentication
        ).getPrincipal()
      )
        .thenReturn(user);
        when(resumeRepository.save(resume));
        assertThat(resumeServices.saveResume(multipartFile)).isNotNull();
    }else{}
  }
}
