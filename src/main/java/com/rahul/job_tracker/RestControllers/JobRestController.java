package com.rahul.job_tracker.RestControllers;

import com.rahul.job_tracker.DTO.JobPostDTO;
import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Services.JobPostServices;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.annotation.Validated;
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

  @GetMapping("/v1/all-jobs")
  public List<JobPostDTO> allJobPosts() {
    return jobPostServices.allJobPosts();
  }

  @PostMapping("/v1/add-job")
  public ResponseEntity<JobPostDTO> addJob(@RequestBody JobPost jobPost) {
    return jobPostServices.createJobPosts(jobPost);
  }

  @PutMapping("/v1/set-resume")
  public ResponseEntity<JobPostDTO> setResume(
    @RequestBody JobPost jobPost,
    @RequestParam UUID resumeID
  ) {
    return jobPostServices.setResume(jobPost, resumeID);
  }

  @DeleteMapping("/v1/delete-job-post")
  public ResponseEntity<String> deleteUsersJobPost(
    @RequestParam UUID jobPostId
  ) {
    return jobPostServices.deleteUsersJobPost(jobPostId);
  }

  @GetMapping("/v1/retrieve-user-job-posts")
  public ResponseEntity<List<JobPost>> retrieveUserJobPosts() {
    return jobPostServices.retrieveUserJobPosts();
  }

  @GetMapping("/v1/count-user-job-posts")
  public ResponseEntity<Integer> countUserJobPosts() {
    return jobPostServices.countUserJobPosts();
  }

  @GetMapping("/v1/retrieve-job-post-with-user-id")
  public JobPost retrieveJobPostWithUserId(UUID jobPostId) {
    return jobPostServices.retrieveUserJobPostWithId(jobPostId);
  }

  @PutMapping("/v1/update-job-post")
  public ResponseEntity<String> updateJobPost(@RequestBody JobPost jobPost) {
    return jobPostServices.updateJobPost(jobPost);
  }

  @PostMapping("/v1/add-job-with-job-id")
  public ResponseEntity<String> addJobWithJobId(@RequestParam UUID jobPostId) {
    // if(jobPostServices.checkJobPostInUserJobList(jobPostId)){
    //   return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Job post already exists with id: " + jobPostId);
    // }
    return jobPostServices.addJobWithJobId(jobPostId);
  }

  @GetMapping("/v1/retrive-users-per-day-jobposts")
  public List<Object[]> retrieveUsersPerDayJobPosts() {
    return jobPostServices.retrieveUsersPerDayJobPosts();
  }
}
