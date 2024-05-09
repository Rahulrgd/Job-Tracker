package com.rahul.job_tracker.Services;

import com.rahul.job_tracker.DTO.JobPostDTO;
import com.rahul.job_tracker.DTO.TopPerformerDTO;
import com.rahul.job_tracker.DTO.UserDTO;
import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Entities.JobStatusEnum;
import com.rahul.job_tracker.Entities.User;
import com.rahul.job_tracker.Repositories.JobPostRepository;
import com.rahul.job_tracker.Repositories.UserRepository;
import com.rahul.job_tracker.Resume.Resume;
import com.rahul.job_tracker.Resume.ResumeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Slf4j
public class JobPostServices {

  @Autowired
  private JobPostRepository jobPostRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ResumeRepository resumeRepository;

  // ==================================Get User from Security Context Holder==================
  private User getUser() {
    return (User) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
  }

  // ==========================Retrieve All JobPosts===========================================

  public List<JobPostDTO> allJobPosts() {
    Sort sort = Sort.by("jobDate").descending();
    return jobPostRepository
      .findAll(sort)
      .stream()
      .map(jobpost -> jobpost.toDTO())
      .filter(jobpost -> jobpost.getClone() == false)
      .collect(Collectors.toList());
  }

  // =============================Create Job Post=====================================

  public ResponseEntity<String> createJobPosts(JobPost jobPost) {
    User user = getUser();
    jobPost.setUser(user);
    jobPost.setClone(false);
    if(jobPost.getJobDate()==null){
      jobPost.setJobDate(LocalDate.now());
    }
    jobPostRepository.save(jobPost);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("Job post created Successfully");
  }

  // =============================Set Resume to Job Post======================================
  public ResponseEntity<JobPostDTO> setResume(JobPost jobPost, UUID resumeId) {
    Optional<Resume> optionalResume = resumeRepository.findById(resumeId);
    Resume resume = optionalResume.orElseThrow(() ->
      new IllegalArgumentException("Resume Does Not Exist!!")
    );
    jobPost.setResume(resume);
    jobPostRepository.save(jobPost);
    return ResponseEntity.status(HttpStatus.OK).body(jobPost.toDTO());
  }

  // =================================Delete User's Job Post======================================
  public ResponseEntity<String> deleteUsersJobPost(UUID jobPostId) {
    Optional<JobPost> optionalJobPost = jobPostRepository.findById(jobPostId);
    JobPost jobPost = optionalJobPost.orElseThrow(() ->
      new IllegalArgumentException(
        "JobPost does Not Exist with Id: " + jobPostId
      )
    );
    User user = getUser();
    User jobPostUser = jobPost.getUser();
    jobPostRepository.deleteById(jobPostId);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body("Job post deleted successfully.");
  }

  // =============================Retrieve User's Job Posts=================================
  public ResponseEntity<List<JobPostDTO>> retrieveUserJobPosts() {
    Sort sort = Sort.by("jobDate").descending();
    User user = getUser();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        jobPostRepository
          .findByUser(user, sort)
          .stream()
          .map(jobpost -> jobpost.toDTO())
          .collect(Collectors.toList())
      );
  }

  // ==============================Count User Job Posts=====================================
  public ResponseEntity<Integer> countUserJobPosts() {
    User user = getUser();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(jobPostRepository.countByUser(user));
  }

  // =========================Retrieve User JobPost With ID===============================
  public JobPost retrieveUserJobPostWithId(UUID jobPostId) {
    Optional<JobPost> optionalJobPost = jobPostRepository.findById(jobPostId);
    JobPost jobPost = optionalJobPost.orElseThrow(() ->
      new IllegalArgumentException("No job post found with id: " + jobPostId)
    );
    return jobPost;
  }

  // ============================Update Job Posts=============================
  public ResponseEntity<String> updateJobPost(JobPost jobPost) {
    Optional<JobPost> optionalJobPost = jobPostRepository.findById(
      jobPost.getId()
    );
    JobPost existingJobPost = optionalJobPost.orElseThrow(() ->
      new IllegalArgumentException(
        "Job post does not exist with id: " + jobPost.getId()
      )
    );

    if (jobPost.getJobTitle() != null) {
      existingJobPost.setJobTitle(jobPost.getJobTitle());
    }

    if (jobPost.getCompanyName() != null) {
      existingJobPost.setCompanyName(jobPost.getCompanyName());
    }

    if (jobPost.getJobDescription() != null) {
      existingJobPost.setJobDescription(jobPost.getJobDescription());
    }

    if (jobPost.getJobLink() != null) {
      existingJobPost.setJobLink(jobPost.getJobLink());
    }

    if (jobPost.getStatus() != null) {
      existingJobPost.setStatus(jobPost.getStatus());
    }

    if (jobPost.getJobDate() != null) {
      existingJobPost.setJobDate(jobPost.getJobDate());
    }

    try {
      jobPostRepository.save(existingJobPost);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Updation failed due to: " + e.getMessage());
    }
    return ResponseEntity
      .status(HttpStatus.OK)
      .body("Job post updated successfully;");
  }

  // =============================Clone Job Post==================================
  public ResponseEntity<String> addJobWithJobId(UUID jobPostid) {
    Optional<JobPost> optionalJobPost = jobPostRepository.findById(jobPostid);
    JobPost oldJobPost = optionalJobPost.orElseThrow(() ->
      new IllegalArgumentException("Job does not exist with id: " + jobPostid)
    );
    JobStatusEnum status = JobStatusEnum.BOOKMARKED;
    JobPost newJobPost = new JobPost();
    newJobPost.setUser(getUser());
    newJobPost.setJobTitle(oldJobPost.getJobTitle());
    newJobPost.setCompanyName(oldJobPost.getCompanyName());
    newJobPost.setJobDescription(oldJobPost.getJobDescription());
    newJobPost.setJobLink(oldJobPost.getJobLink());
    newJobPost.setClone(true);
    newJobPost.setStatus(status);
    newJobPost.setJobDate(LocalDate.now());
    jobPostRepository.save(newJobPost);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("Job post added successfully to your account.");
  }

  // =======================Check Job Post Exists in User Account or Not========================
  public boolean checkJobPostInUserJobList(UUID jobPostId) {
    return getUser()
      .getJobPosts()
      .contains(jobPostRepository.findById(jobPostId));
  }

  // ==========================Retrieve User's Job Posts per Day==================================
  public List<Object[]> retrieveUsersPerDayJobPosts() {
    return jobPostRepository.countUsersPostPerDay(getUser());
  }

  // ==========================Top 3 Performer's of the day with their Job Counts ===================
  public ResponseEntity<List<TopPerformerDTO>> retrieveTopPerformersOfTheDay() {
    LocalDate date = LocalDate.now();
    List<Object[]> results = jobPostRepository.topPerformersOfTheDay(date);

    List<TopPerformerDTO> topPerformerDTOs = results
      .stream()
      .map(item -> {
        User user = (User) item[0];
        Long count = (Long) item[1];

        // Map user details to UserDTO
        UserDTO userDTO = user.toDTO();
        TopPerformerDTO result = new TopPerformerDTO();
        result.setFullName(userDTO.getFullName());
        result.setJobPostCount(count);
        return result;
      })
      .collect(Collectors.toList());
    log.info("Value of List Top Performers: " + topPerformerDTOs);

    return ResponseEntity.status(HttpStatus.OK).body(topPerformerDTOs);
  }

  // ==========================Retrive Jobposts with Filters Applied==================================
  public ResponseEntity<List<JobPostDTO>> retrieveJobsByFilters(
    String jobTitle,
    String companyName,
    String jobDescription,
    LocalDate jobDate,
    JobStatusEnum status
  ) {
    List<JobPostDTO> filteredJobPostDTO = jobPostRepository
      .findJobPostByFilters(
        jobTitle,
        companyName,
        jobDescription,
        jobDate,
        status
      )
      .stream()
      .map(jobPost -> jobPost.toDTO())
      .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(filteredJobPostDTO);
  }

  // ==================================Retrive Job Posts Containting String==========================================
  public ResponseEntity<List<JobPostDTO>> retriveJobPostsContaingString(
    String string
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        jobPostRepository
          .findJobPostContaingString(string)
          .stream()
          .map(jobpost -> jobpost.toDTO())
          .filter(jobpost -> jobpost.getClone() != true)
          .collect(Collectors.toList())
      );
  }

  // ===================================Retrive User Job Posts Containting String======================================================
  public ResponseEntity<List<JobPostDTO>> retriveUserJobPostsContaingString(
    String string
  ) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        jobPostRepository
          .findUserJobPostContaingString(getUser(), string)
          .stream()
          .map(jobpost -> jobpost.toDTO())
          .filter(jobpost -> jobpost.getClone() != true)
          .collect(Collectors.toList())
      );
  }

  // ===================================Retrive Job Counts Per Day===============================
  public ResponseEntity<List<Object[]>> retrieveJobCountsPerDay() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(jobPostRepository.findJobCountPerDay());
  }
}
