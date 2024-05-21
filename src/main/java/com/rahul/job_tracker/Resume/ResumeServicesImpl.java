package com.rahul.job_tracker.Resume;

import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeDTO;
import com.rahul.job_tracker.User.User;
import com.rahul.job_tracker.User.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeServicesImpl implements ResumeServices {

  @Autowired
  private ResumeRepository resumeRepository;

  public User getUser() {
    return (User) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
  }

  public List<ResumeDTO> getAllResume() {
    return resumeRepository
      .findAll()
      .stream()
      .map(resume -> ResumeMapper.INSTANCE.toDTO(resume))
      .collect(Collectors.toList());
  }

  public String saveResume(MultipartFile file) {
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
    resume.setUser(getUser());
    resumeRepository.save(resume);
    return "Resume saved successfully.";
  }

  public int countUserResumes() {
    return resumeRepository.countByUser(getUser());
  }

  public List<ResumeDTO> retrieveUserResumes() {
    List<Resume> resumes = resumeRepository.findByUser(getUser());
    return resumes
      .stream()
      .map(resume -> ResumeMapper.INSTANCE.toDTO(resume))
      .collect(Collectors.toList());
  }

  public String deleteUserResume(UUID resumeId) {
    resumeRepository.deleteById(resumeId);
    return "Resume deleted successfully.";
  }

  public byte[] downloadUserResume(UUID resumeId) {
    return resumeRepository.findById(resumeId).orElseThrow().getResumeFile();
  }
}
