package com.rahul.job_tracker.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.job_tracker.Entities.Resume;
import com.rahul.job_tracker.UserClasses.User;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID>{
    public int countByUser(User user);
}
