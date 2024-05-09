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

@RestController
public class ResumeRestController {

  @Autowired
  private ResumeServices resumeServices;

  @GetMapping("/v1/all-resume")
  public List<Resume> getAllResume() {
    return resumeServices.getAllResume();
  }

  @PostMapping("/v1/create-resume")
  public ResponseEntity<Resume> createResume(
    @RequestParam("file") MultipartFile file
  ) {
    try {
      Resume savedResume = resumeServices.saveResume(file);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedResume);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @GetMapping("/v1/user-resume-count")
  public int countUsersResume() {
    return resumeServices.countUserResumes();
  }

  @GetMapping("/v1/retrieve-user-resumes")
  public List<ResumeDTO> retrieveUserResume() {
    return resumeServices.retrieveUserResumes();
  }

  @DeleteMapping("/v1/delete-user-resume")
  public ResponseEntity<String> deleteUserResume(UUID resumeId) {
    return resumeServices.deleteUserResume(resumeId);
  }

  @GetMapping("/v1/download-user-resume")
  public ResponseEntity<byte[]> downloadUserResume(@RequestParam UUID resumeId){
    return resumeServices.downloadUserResume(resumeId);
  }
}