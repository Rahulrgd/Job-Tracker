package com.rahul.job_tracker.Resume;

import static org.assertj.core.api.Assertions.assertThat;

import com.rahul.job_tracker.User.User;
import com.rahul.job_tracker.User.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ResumeRepositoryTest {

  @Autowired
  private ResumeRepository resumeRepository;

  @Autowired
  private UserRepository userRepository;

  private User user;
  private List<Resume> resumes;

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
    resumes = new ArrayList<>();
  }

  @Test
  void testCountByUser() {
    assertThat(resumeRepository.countByUser(user)).isNotNull();
  }

  @Test
  void testFindByUser() {
    userRepository.save(user);
    assertThat(resumeRepository.findByUser(user)).isNotNull();
  }
}
