package com.rahul.job_tracker.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rahul.job_tracker.DTO.JobPostDTO;
import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Entities.Resume;
import com.rahul.job_tracker.Repositories.JobPostRepository;
import com.rahul.job_tracker.Repositories.ResumeRepository;
import com.rahul.job_tracker.UserClasses.User;
import com.rahul.job_tracker.UserClasses.UserRepository;

@Service
public class JobPostServices {
    
    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    public List<JobPost> allJobPosts(){
        return jobPostRepository.findAll();
    }

    public ResponseEntity<JobPostDTO> createJobPosts(JobPost jobPost){
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobPost.setUser(user);
        jobPostRepository.save(jobPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPost.toDTO());
    }

    public ResponseEntity<JobPostDTO> setResume(JobPost jobPost, UUID resumeId){
        Optional<Resume> optionalResume = resumeRepository.findById(resumeId);
        Resume resume = optionalResume.orElseThrow(()-> new IllegalArgumentException("Resume Does Not Exist!!"));
        jobPost.setResume(resume);
        jobPostRepository.save(jobPost);
        return ResponseEntity.status(HttpStatus.OK).body(jobPost.toDTO());
    }

    public ResponseEntity<String> deleteUsersJobPost(UUID jobPostId){
        Optional<JobPost> optionalJobPost = jobPostRepository.findById(jobPostId);
        JobPost jobPost = optionalJobPost.orElseThrow(()-> new IllegalArgumentException("JobPost does Not Exist with Id: " + jobPostId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User jobPostUser = jobPost.getUser();
        // if(user.getId() == jobPostUser.getId()){
            jobPostRepository.deleteById(jobPostId);
            return ResponseEntity.status(HttpStatus.OK).body("Job post deleted successfully.");
        // }
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sorry, post can not be deleted.");

    }

    public ResponseEntity<List<JobPost>> retrieveUserJobPosts(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(jobPostRepository.findByUser(user));
    }
}
