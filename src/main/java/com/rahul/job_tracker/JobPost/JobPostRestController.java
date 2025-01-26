package com.rahul.job_tracker.JobPost;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobPostRestController {

  @Autowired
  private JobPostServices jobPostServices;

  // =============================Retrieve All Job Posts==========================================
  @GetMapping("/v1/dashboard/all-jobs")
  public List<JobPostDTO> allJobPosts(@RequestParam int pageNumber) {
    return jobPostServices.allJobPosts(pageNumber);
  }

  // =============================Retrieve Job Post With ID==========================================
  @GetMapping("/v1/dashboard/job-post-with-id")
  public JobPostDTO jobPostWithID(@RequestParam UUID jobPostId) {
    return jobPostServices.retrieveJobPostWithId(jobPostId);
  }

  // =============================Add User's Job Post==========================================
  @PostMapping("/v1/add-job")
  public ResponseEntity<String> addJob(
    @Valid @RequestBody JobPost jobPost,
    BindingResult bindingResult
  ) {
    try {
      if (bindingResult.hasErrors()) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
          errorMessage.append(error.getDefaultMessage()).append(" ");
        }
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(errorMessage.toString());
      }
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(jobPostServices.createJobPosts(jobPost));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // =============================Set Resume to User's Job Post==========================================
  @PutMapping("/v1/set-resume")
  public ResponseEntity<?> setResume(
    @RequestBody JobPost jobPost,
    @RequestParam UUID resumeID
  ) {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.setResume(jobPost, resumeID));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ================================Delete User's Job Post=======================================
  @DeleteMapping("/v1/delete-job-post")
  public ResponseEntity<String> deleteUsersJobPost(
    @RequestParam UUID jobPostId
  ) {
    try {
      jobPostServices.deleteUsersJobPost(jobPostId);
      return ResponseEntity
        .status(HttpStatus.OK)
        .body("Jobpost deleted successfully, with ID: " + jobPostId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ===============================Retrieve User's Job Posts========================================
  @GetMapping("/v1/retrieve-user-job-posts")
  public ResponseEntity<?> retrieveUserJobPosts(@RequestParam int pageNumber) {
    // return jobPostServices.retrieveUserJobPosts();
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.retrieveUserJobPosts(pageNumber));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ============================Count User's Job Posts===========================================
  @GetMapping("/v1/count-user-job-posts")
  public ResponseEntity<?> countUserJobPosts() {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.countUserJobPosts());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ================================Retrive Job Post with Job Post ID=======================================
  @GetMapping("/v1/retrieve-job-post-with-user-id")
  public ResponseEntity<?> retrieveJobPostWithJobPostId(UUID jobPostId) {
    // return jobPostServices.retrieveUserJobPostWithId(jobPostId);
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.retrieveUserJobPostWithId(jobPostId));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ================================Update Job Post Details=======================================
  @PutMapping("/v1/update-job-post")
  public ResponseEntity<String> updateJobPost(@RequestBody JobPost jobPost) {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.updateJobPost(jobPost));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // =================================Add Job Posts From Dashboard to User's Account======================================
  @PostMapping("/v1/add-job-with-job-id")
  public ResponseEntity<String> addJobWithJobId(@RequestParam UUID jobPostId) {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.addJobWithJobId(jobPostId));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ==================================Retrieve User's Job Posts Count Of Each Day=====================================
  @GetMapping("/v1/retrive-users-per-day-jobposts")
  public List<Object[]> retrieveUsersPerDayJobPosts(@RequestParam int pageNumber) {
    return jobPostServices.retrieveUsersPerDayJobPosts(pageNumber);
  }

  // ====================================Top 3 Performer's of the day with their Job Counts ===========================
  @GetMapping(
    "/v1/dashboard/top-three-performer-of-the-day-with-their-job-count"
  )
  public ResponseEntity<?> topPerformersOfTheDay() {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.retrieveTopPerformersOfTheDay());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // =============================Retrieve JobPosts with Filters Applied===================================
  @GetMapping("/v1/retrieve-job-posts-with-filters-applied")
  public ResponseEntity<?> retrieveJobPostsWithFiltersApplied(
    @RequestParam String jobTitle,
    @RequestParam String companyName,
    @RequestParam String jobDescription,
    @RequestParam LocalDate jobDate,
    @RequestParam JobStatusEnum status
  ) {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(
          jobPostServices.retrieveJobsByFilters(
            jobTitle,
            companyName,
            jobDescription,
            jobDate,
            status
          )
        );
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ========================================Retrive JobPosts With String=============================================
  @GetMapping("/v1/dashboard/search-jobposts-containing-strings")
  public ResponseEntity<?> retriveJobPostsWithString(
    @RequestParam String string,
    @RequestParam int pageNumber
  ) {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.retriveJobPostsContaingString(string, pageNumber));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // ======================================Retrieve User Job Posts Containg String===================================
  @GetMapping("/v1/search-user-jobposts-containing-string")
  public ResponseEntity<?> retrieveUserJobPostsContaingString(
    @RequestParam String string, @RequestParam int pageNumber
  ) {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.retriveUserJobPostsContaingString(string, pageNumber));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // =======================================Retrive Job Post Count Per Day================================
  @GetMapping("/v1/dashboard/retrive-jobpost-count-per-day")
  public ResponseEntity<?> retrieveJobCountsPerDay(@RequestParam int pageNumber) {
    try {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body(jobPostServices.retrieveJobCountsPerDay(pageNumber));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
