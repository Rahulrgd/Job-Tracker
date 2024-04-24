package com.rahul.job_tracker.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.job_tracker.Services.JobPostServices;

@RestController
public class HomePage {
    @Autowired
    JobPostServices jobPostServices;
    
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to Job Tracking Web Application!";
    }
}
