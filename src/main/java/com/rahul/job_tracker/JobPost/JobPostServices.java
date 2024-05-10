package com.rahul.job_tracker.JobPost;

import com.rahul.job_tracker.Dashboard.TopPerformerDTO;
import com.rahul.job_tracker.User.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface JobPostServices {
  public User getUser();

  public List<JobPostDTO> allJobPosts();

  public ResponseEntity<String> createJobPosts(JobPost jobPost);

  public ResponseEntity<JobPostDTO> setResume(JobPost jobPost, UUID resumeId);

  public ResponseEntity<String> deleteUsersJobPost(UUID jobPostId);

  public ResponseEntity<List<JobPostDTO>> retrieveUserJobPosts();

  public ResponseEntity<Integer> countUserJobPosts();

  public JobPost retrieveUserJobPostWithId(UUID jobPostId);

  public ResponseEntity<String> updateJobPost(JobPost jobPost);

  public ResponseEntity<String> addJobWithJobId(UUID jobPostid);

  public boolean checkJobPostInUserJobList(UUID jobPostId);

  public List<Object[]> retrieveUsersPerDayJobPosts();

  public ResponseEntity<List<TopPerformerDTO>> retrieveTopPerformersOfTheDay();

  public ResponseEntity<List<JobPostDTO>> retrieveJobsByFilters(
    String jobTitle,
    String companyName,
    String jobDescription,
    LocalDate jobDate,
    JobStatusEnum status
  );

  public ResponseEntity<List<JobPostDTO>> retriveJobPostsContaingString(
    String string
  );

  public ResponseEntity<List<JobPostDTO>> retriveUserJobPostsContaingString(
    String string
  );

  public ResponseEntity<List<Object[]>> retrieveJobCountsPerDay();
}
