package com.rahul.job_tracker.RestControllers;

import com.rahul.job_tracker.DTO.JobPostDTO;
import com.rahul.job_tracker.DTO.TopPerformerDTO;
import com.rahul.job_tracker.DTO.UserDTO;
import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Entities.JobStatusEnum;
import com.rahul.job_tracker.Services.JobPostServices;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobRestController {

  @Autowired
  private JobPostServices jobPostServices;

  // =============================Retrieve All Job Posts==========================================
  @GetMapping("/v1/all-jobs")
  public List<JobPostDTO> allJobPosts() {
    return jobPostServices.allJobPosts();
  }

  // =============================Add User's Job Post==========================================
  @PostMapping("/v1/add-job")
  public ResponseEntity<String> addJob(
    @Valid @RequestBody JobPost jobPost,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      StringBuilder errorMessage = new StringBuilder();
      for (FieldError error : bindingResult.getFieldErrors()) {
        errorMessage.append(error.getDefaultMessage()).append(" ");
      }
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorMessage.toString());
    }
    return jobPostServices.createJobPosts(jobPost);
  }

  // =============================Set Resume to User's Job Post==========================================
  @PutMapping("/v1/set-resume")
  public ResponseEntity<JobPostDTO> setResume(
    @RequestBody JobPost jobPost,
    @RequestParam UUID resumeID
  ) {
    return jobPostServices.setResume(jobPost, resumeID);
  }

  // ================================Delete User's Job Post=======================================
  @DeleteMapping("/v1/delete-job-post")
  public ResponseEntity<String> deleteUsersJobPost(
    @RequestParam UUID jobPostId
  ) {
    return jobPostServices.deleteUsersJobPost(jobPostId);
  }

  // ===============================Retrieve User's Job Posts========================================
  @GetMapping("/v1/retrieve-user-job-posts")
  public ResponseEntity<List<JobPost>> retrieveUserJobPosts() {
    return jobPostServices.retrieveUserJobPosts();
  }

  // ============================Count User's Job Posts===========================================
  @GetMapping("/v1/count-user-job-posts")
  public ResponseEntity<Integer> countUserJobPosts() {
    return jobPostServices.countUserJobPosts();
  }

  // ================================Retrive Job Post with Job Post ID=======================================
  @GetMapping("/v1/retrieve-job-post-with-user-id")
  public JobPost retrieveJobPostWithUserId(UUID jobPostId) {
    return jobPostServices.retrieveUserJobPostWithId(jobPostId);
  }

  // ================================Update Job Post Details=======================================
  @PutMapping("/v1/update-job-post")
  public ResponseEntity<String> updateJobPost(@RequestBody JobPost jobPost) {
    return jobPostServices.updateJobPost(jobPost);
  }

  // =================================Add Job Posts From Dashboard to User's Account======================================
  @PostMapping("/v1/add-job-with-job-id")
  public ResponseEntity<String> addJobWithJobId(@RequestParam UUID jobPostId) {
    // if(jobPostServices.checkJobPostInUserJobList(jobPostId)){
    //   return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Job post already exists with id: " + jobPostId);
    // }
    return jobPostServices.addJobWithJobId(jobPostId);
  }

  // ==================================Retrieve User's Job Posts Count Of Each Day=====================================
  @GetMapping("/v1/retrive-users-per-day-jobposts")
  public List<Object[]> retrieveUsersPerDayJobPosts() {
    return jobPostServices.retrieveUsersPerDayJobPosts();
  }

  // ====================================Top 3 Performer's of the day with their Job Counts ===========================
  @GetMapping("/v1/top-three-performer-of-the-day-with-their-job-count")
  public ResponseEntity<List<TopPerformerDTO>> topPerformersOfTheDay() {
    return jobPostServices.retrieveTopPerformersOfTheDay();
  }

  // =============================Retrieve JobPosts with Filters Applied===================================
  @GetMapping("/v1/retrieve-job-posts-with-filters-applied")
  public ResponseEntity<List<JobPostDTO>> retrieveJobPostsWithFiltersApplied(
    @RequestParam String jobTitle,
    @RequestParam String companyName,
    @RequestParam String jobDescription,
    @RequestParam LocalDate jobDate,
    @RequestParam JobStatusEnum status
  ) {
    return jobPostServices.retrieveJobsByFilters(
      jobTitle,
      companyName,
      jobDescription,
      jobDate,
      status
    );
  }
}
