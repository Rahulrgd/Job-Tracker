package com.rahul.job_tracker.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.job_tracker.Entities.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>{
    
}
