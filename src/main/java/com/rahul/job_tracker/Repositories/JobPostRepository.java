package com.rahul.job_tracker.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.job_tracker.Entities.JobPost;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost,Long>{
}
