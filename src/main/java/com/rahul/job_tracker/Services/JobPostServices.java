package com.rahul.job_tracker.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Service;

import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Repositories.JobPostRepository;

@Service
public class JobPostServices {
    
    @Autowired
    private JobPostRepository jobPostRepository;

    public List<JobPost> allJobPosts(){
        return jobPostRepository.findAll();
    }

    public ResponseEntity<JobPost> createJobPosts(JobPost jobPost){
        jobPostRepository.save(jobPost);
        // return ResponseEntity.ok(jobPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPost);
    }
}
