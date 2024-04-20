package com.rahul.job_tracker.RestControllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.job_tracker.DTO.JobPostDTO;
import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Services.JobPostServices;

@RestController
public class JobRestController {
    @Autowired
    private JobPostServices jobPostServices;

    @GetMapping("/all_jobs")
    public List<JobPost> allJobPosts(){
        return jobPostServices.allJobPosts();
    }

    @PostMapping("/add_job")
    public ResponseEntity<JobPostDTO> addJob(@RequestBody JobPost jobPost){
        return jobPostServices.createJobPosts(jobPost);
    }

    @PutMapping("/set-resume")
    public ResponseEntity<JobPostDTO> setResume(@RequestBody JobPost jobPost,@RequestParam UUID resumeID){
        return jobPostServices.setResume(jobPost, resumeID);
    }



}
