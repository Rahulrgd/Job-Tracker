package com.rahul.job_tracker.Resume;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeDTO;
import com.rahul.job_tracker.User.User;

@Service
public interface ResumeServices {

  // User getUser();

  public List<ResumeDTO> getAllResume();

  public String saveResume(MultipartFile file);

  public int countUserResumes();

  public List<ResumeDTO> retrieveUserResumes();

  public String deleteUserResume(UUID resumeId);

  public byte[] downloadUserResume(UUID resumeId);
}
