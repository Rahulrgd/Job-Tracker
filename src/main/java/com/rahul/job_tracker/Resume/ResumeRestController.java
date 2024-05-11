package com.rahul.job_tracker.Resume;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeDTO;

@RestController
public class ResumeRestController {

  @Autowired
  private ResumeServices resumeServices;

  @GetMapping("/v1/all-resume")
  public List<ResumeDTO> getAllResume() {
    return resumeServices.getAllResume();
  }

  @PostMapping("/v1/create-resume")
  public ResponseEntity<?> createResume(
    @RequestParam("file") MultipartFile file
  ) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED).body(resumeServices.saveResume(file));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/v1/user-resume-count")
  public ResponseEntity<?> countUsersResume() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(resumeServices.countUserResumes());
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/v1/retrieve-user-resumes")
  public ResponseEntity<?> retrieveUserResume() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(resumeServices.retrieveUserResumes());
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/v1/delete-user-resume")
  public ResponseEntity<String> deleteUserResume(UUID resumeId) {
    try {
      resumeServices.deleteUserResume(resumeId);
      return ResponseEntity.status(HttpStatus.OK).body("Resume Deleted Successfully with ID: " + resumeId);
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/v1/download-user-resume")
  public ResponseEntity<?> downloadUserResume(@RequestParam UUID resumeId){
    try {
      return ResponseEntity.status(HttpStatus.OK).body(resumeServices.downloadUserResume(resumeId));
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
