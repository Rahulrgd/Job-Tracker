package com.rahul.job_tracker.JobPost;

import com.rahul.job_tracker.Dashboard.TopPerformerDTO;
import com.rahul.job_tracker.User.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface JobPostServices {
  public User getUser();

  public List<JobPostDTO> allJobPosts(int pageNumber);

  public String createJobPosts(JobPost jobPost);

  public JobPostDTO setResume(JobPost jobPost, UUID resumeId);

  public String deleteUsersJobPost(UUID jobPostId);

  public List<JobPostDTO> retrieveUserJobPosts();

  public Integer countUserJobPosts();

  public JobPost retrieveUserJobPostWithId(UUID jobPostId);

  public String updateJobPost(JobPost jobPost);

  public String addJobWithJobId(UUID jobPostid);

  public boolean checkJobPostInUserJobList(UUID jobPostId);

  public List<Object[]> retrieveUsersPerDayJobPosts();

  public List<TopPerformerDTO> retrieveTopPerformersOfTheDay();

  public List<JobPostDTO> retrieveJobsByFilters(
    String jobTitle,
    String companyName,
    String jobDescription,
    LocalDate jobDate,
    JobStatusEnum status
  );

  public List<JobPostDTO> retriveJobPostsContaingString(
    String string
  );

  public List<JobPostDTO> retriveUserJobPostsContaingString(
    String string
  );

  public List<Object[]> retrieveJobCountsPerDay();
}
