package com.rahul.job_tracker.Resume;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.job_tracker.User.User;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
  public int countByUser(User user);

  public List<Resume> findByUser(User user);
}
