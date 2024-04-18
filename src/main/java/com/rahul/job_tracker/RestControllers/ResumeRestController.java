package com.rahul.job_tracker.RestControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rahul.job_tracker.Entities.Resume;
import com.rahul.job_tracker.Services.ResumeServices;

@RestController
public class ResumeRestController {

    @Autowired
    private ResumeServices resumeServices;

    @GetMapping("/all-resume")
    public List<Resume> getAllResume(){
        return resumeServices.getAllResume();
    }

    @PostMapping("/create-resume")
    public ResponseEntity<Resume> createResume(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId){
        return resumeServices.saveResume(file,userId);
    }
}
