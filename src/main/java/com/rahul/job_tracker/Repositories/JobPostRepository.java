package com.rahul.job_tracker.Repositories;

import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.UserClasses.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, UUID> {
  public List<JobPost> findByUser(User user);
}
