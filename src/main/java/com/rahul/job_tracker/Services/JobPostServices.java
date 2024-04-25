package com.rahul.job_tracker.Services;

import com.rahul.job_tracker.DTO.JobPostDTO;
import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Entities.JobStatusEnum;
import com.rahul.job_tracker.Entities.Resume;
import com.rahul.job_tracker.Repositories.JobPostRepository;
import com.rahul.job_tracker.Repositories.ResumeRepository;
import com.rahul.job_tracker.UserClasses.User;
import com.rahul.job_tracker.UserClasses.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class JobPostServices {

  @Autowired
  private JobPostRepository jobPostRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ResumeRepository resumeRepository;

  private User getUser() {
    return (User) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
  }

  public List<JobPostDTO> allJobPosts() {
    return jobPostRepository
      .findAll()
      .stream()
      .map(jobpost -> jobpost.toDTO())
      .collect(Collectors.toList());
  }

  public ResponseEntity<JobPostDTO> createJobPosts(JobPost jobPost) {
    User user = getUser();
    jobPost.setUser(user);
    jobPostRepository.save(jobPost);
    return ResponseEntity.status(HttpStatus.CREATED).body(jobPost.toDTO());
  }

  public ResponseEntity<JobPostDTO> setResume(JobPost jobPost, UUID resumeId) {
    Optional<Resume> optionalResume = resumeRepository.findById(resumeId);
    Resume resume = optionalResume.orElseThrow(() ->
      new IllegalArgumentException("Resume Does Not Exist!!")
    );
    jobPost.setResume(resume);
    jobPostRepository.save(jobPost);
    return ResponseEntity.status(HttpStatus.OK).body(jobPost.toDTO());
  }

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

  public ResponseEntity<List<JobPost>> retrieveUserJobPosts() {
    User user = getUser();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(jobPostRepository.findByUser(user));
  }

  public ResponseEntity<Integer> countUserJobPosts() {
    User user = getUser();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(jobPostRepository.countByUser(user));
  }

  public JobPost retrieveUserJobPostWithId(UUID jobPostId) {
    Optional<JobPost> optionalJobPost = jobPostRepository.findById(jobPostId);
    JobPost jobPost = optionalJobPost.orElseThrow(() ->
      new IllegalArgumentException("No job post found with id: " + jobPostId)
    );
    return jobPost;
  }

  // public ResponseEntity<String> updateJobPost(UUID jobPostId, JobPost jobPost){
  public ResponseEntity<String> updateJobPost(JobPost jobPost) {
    // Optional<JobPost> optionalJobPost = jobPostRepository.findById(jobPostId);
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

  public ResponseEntity<String> addJobWithJobId(UUID jobPostid) {
    if (jobPostRepository.existsById(jobPostid)) {
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
      newJobPost.setStatus(status);
      newJobPost.setJobDate(LocalDate.now());
      jobPostRepository.save(newJobPost);
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("Job post added successfully to your account.");
    }
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body("Job already exists in your account.");
  }
}
