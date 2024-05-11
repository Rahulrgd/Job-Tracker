package com.rahul.job_tracker.JobPost;

import com.rahul.job_tracker.Dashboard.TopPerformerDTO;
import com.rahul.job_tracker.Resume.Resume;
import com.rahul.job_tracker.Resume.ResumeRepository;
import com.rahul.job_tracker.User.User;
import com.rahul.job_tracker.User.UserDTO;
import com.rahul.job_tracker.User.UserMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobPostServicesImpl implements JobPostServices {

  @Autowired
  private JobPostRepository jobPostRepository;

  @Autowired
  private ResumeRepository resumeRepository;

  // ==================================Get User from Security Context Holder==================
  public User getUser() {
    return (User) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
  }

  // ==========================Retrieve All JobPosts===========================================

  public List<JobPostDTO> allJobPosts(int pageNumber) {
    Sort sort = Sort.by("jobDate").descending();
    Pageable pageable = PageRequest.of(pageNumber, 50, sort);
    Page<JobPost> page = jobPostRepository.findAll(pageable);
    List<JobPostDTO> jobPosts = page
      .stream()
      .map(jobPost -> JobPostMapper.INSTANCE.toDTO(jobPost))
      .filter(jobPost -> !jobPost.getClone())
      .collect(Collectors.toList());

    if (jobPosts.isEmpty()) {
      return Collections.emptyList();
    } else if (jobPosts.size() < 50 && page.hasNext()) {
      Pageable remainingPageable = PageRequest.of(
        1,
        50 - jobPosts.size(),
        sort
      );
      List<JobPostDTO> remainingJobPosts = jobPostRepository
        .findAll(remainingPageable)
        .stream()
        .map(jobPost -> JobPostMapper.INSTANCE.toDTO(jobPost))
        .filter(jobPost -> !jobPost.getClone())
        .collect(Collectors.toList());
      jobPosts.addAll(remainingJobPosts);
    }

    return jobPosts;
  }

  // =============================Create Job Post=====================================

  public String createJobPosts(JobPost jobPost) {
    User user = getUser();
    jobPost.setUser(user);
    jobPost.setClone(false);
    if (jobPost.getJobDate() == null) {
      jobPost.setJobDate(LocalDate.now());
    }
    jobPostRepository.save(jobPost);
    return "Job post created Successfully";
  }

  // =============================Set Resume to Job Post======================================
  public JobPostDTO setResume(JobPost jobPost, UUID resumeId) {
    Optional<Resume> optionalResume = resumeRepository.findById(resumeId);
    Resume resume = optionalResume.orElseThrow(() ->
      new IllegalArgumentException("Resume Does Not Exist!!")
    );
    jobPost.setResume(resume);
    jobPostRepository.save(jobPost);
    return JobPostMapper.INSTANCE.toDTO(jobPost);
  }

  // =================================Delete User's Job Post======================================
  public String deleteUsersJobPost(UUID jobPostId) {
    Optional<JobPost> optionalJobPost = jobPostRepository.findById(jobPostId);
    JobPost jobPost = optionalJobPost.orElseThrow(() ->
      new IllegalArgumentException(
        "JobPost does Not Exist with Id: " + jobPostId
      )
    );
    User user = getUser();
    User jobPostUser = jobPost.getUser();
    jobPostRepository.deleteById(jobPostId);
    return "Job post deleted successfully.";
  }

  // =============================Retrieve User's Job Posts=================================
  public List<JobPostDTO> retrieveUserJobPosts(int pageNumber) {
    Sort sort = Sort.by("jobDate").descending();
    Pageable pageable = PageRequest.of(pageNumber, 50, sort);
    Page<JobPost> page = jobPostRepository.findByUser(getUser(), pageable);
    List<JobPostDTO> jobPosts = page
      .getContent()
      .stream()
      .map(JobPostMapper.INSTANCE::toDTO)
      .collect(Collectors.toList());

      if(jobPosts.isEmpty()){
        return new ArrayList<>();
      }else if(page.hasNext()){
        return jobPosts;
      }else{
        List<JobPostDTO> newJobPosts = page
      .getContent()
      .stream()
      .map(JobPostMapper.INSTANCE::toDTO)
      .collect(Collectors.toList());
      jobPosts.addAll(newJobPosts);
      return jobPosts;
      }
  }

  // ==============================Count User Job Posts=====================================
  public Integer countUserJobPosts() {
    User user = getUser();
    return jobPostRepository.countByUser(user);
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
  public String updateJobPost(JobPost jobPost) {
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
      return "Updation failed due to: " + e.getMessage();
    }
    return "Job post updated successfully";
  }

  // =============================Clone Job Post==================================
  public String addJobWithJobId(UUID jobPostid) {
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
    return "Job post added successfully to your account.";
  }

  // =======================Check Job Post Exists in User Account or Not========================
  public boolean checkJobPostInUserJobList(UUID jobPostId) {
    return getUser()
      .getJobPosts()
      .contains(jobPostRepository.findById(jobPostId));
  }

  // ==========================Retrieve User's Job Posts per Day==================================
  public List<Object[]> retrieveUsersPerDayJobPosts(int pageNumber) {
    int pageSize = 7;
    Sort sort = Sort.by("jobDate").descending();
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
    Page<Object[]> page = jobPostRepository.countUsersPostPerDay(
      getUser(),
      pageable
    );
    List<Object[]> jobPostPerDay = new ArrayList<>(page.getContent());

    if (jobPostPerDay.isEmpty()) {
      return new ArrayList<>();
    } else {
      if (page.hasNext()) {
        return jobPostPerDay;
      } else {
        Page<Object[]> nextPage = jobPostRepository.countUsersPostPerDay(
          getUser(),
          pageable
        );
        jobPostPerDay.addAll(nextPage.getContent());
      }
    }

    return jobPostPerDay;
  }

  // ==========================Top 3 Performer's of the day with their Job Counts ===================
  public List<TopPerformerDTO> retrieveTopPerformersOfTheDay() {
    LocalDate date = LocalDate.now();
    List<Object[]> results = jobPostRepository.topPerformersOfTheDay(date);

    List<TopPerformerDTO> topPerformerDTOs = results
      .stream()
      .map(item -> {
        User user = (User) item[0];
        Long count = (Long) item[1];

        // Map user details to UserDTO

        // UserDTO userDTO = user.toDTO();
        UserDTO userDTO = UserMapper.INSTANCE.toDTO(user);
        TopPerformerDTO result = new TopPerformerDTO();
        result.setFullName(userDTO.getFullName());
        result.setJobPostCount(count);
        return result;
      })
      .collect(Collectors.toList());
    log.info("Value of List Top Performers: " + topPerformerDTOs);

    return topPerformerDTOs;
  }

  // ==========================Retrive Jobposts with Filters Applied==================================
  public List<JobPostDTO> retrieveJobsByFilters(
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
      .map(jobPost -> JobPostMapper.INSTANCE.toDTO(jobPost))
      .collect(Collectors.toList());

    return filteredJobPostDTO;
  }

  // ==================================Retrive Job Posts Containting String==========================================
  public List<JobPostDTO> retriveJobPostsContaingString(String string) {
    return jobPostRepository
      .findJobPostContaingString(string)
      .stream()
      .map(jobpost -> JobPostMapper.INSTANCE.toDTO(jobpost))
      .filter(jobpost -> jobpost.getClone() != true)
      .collect(Collectors.toList());
  }

  // ===================================Retrive User Job Posts Containting String======================================================
  public List<JobPostDTO> retriveUserJobPostsContaingString(String string) {
    return jobPostRepository
      .findUserJobPostContaingString(getUser(), string)
      .stream()
      .map(jobpost -> JobPostMapper.INSTANCE.toDTO(jobpost))
      .filter(jobpost -> jobpost.getClone() != true)
      .collect(Collectors.toList());
  }

  // ===================================Retrive Job Counts Per Day===============================
  public List<Object[]> retrieveJobCountsPerDay() {
    return jobPostRepository.findJobCountPerDay();
  }
}
