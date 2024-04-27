package com.rahul.job_tracker.Repositories;

import com.rahul.job_tracker.Entities.Resume;
import com.rahul.job_tracker.Entities.User;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
  public int countByUser(User user);

  public List<Resume> findByUser(User user);
}
