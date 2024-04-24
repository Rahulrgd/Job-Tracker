package com.rahul.job_tracker.Services;

import com.rahul.job_tracker.Entities.Resume;
import com.rahul.job_tracker.Repositories.ResumeRepository;
import com.rahul.job_tracker.UserClasses.User;
import com.rahul.job_tracker.UserClasses.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeServices {

  @Autowired
  private ResumeRepository resumeRepository;

  @Autowired
  private UserRepository userRepository;

  private User getUser(){
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public List<Resume> getAllResume() {
    return resumeRepository.findAll();
  }

  public Resume saveResume(MultipartFile file) {
    Resume resume = new Resume();
    try {
        byte[] resumeFile = file.getBytes();
        resume.setResume(resumeFile);
        String resumeName = file.getOriginalFilename();
        resume.setResumeName(resumeName);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new RuntimeException("Failed to save resume", e);
    }
    User user = getUser();
    resume.setUser(user);
    return resumeRepository.save(resume);
    // return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  public int countUserResumes(){
    User user = getUser();
    return resumeRepository.countByUser(null);
  }
}

