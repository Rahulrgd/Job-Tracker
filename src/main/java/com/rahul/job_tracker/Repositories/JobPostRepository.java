package com.rahul.job_tracker.Repositories;

import com.rahul.job_tracker.Entities.JobPost;
import com.rahul.job_tracker.Entities.JobStatusEnum;
import com.rahul.job_tracker.Entities.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, UUID> {
  String sortBy = "jobDate";

  public List<JobPost> findByUser(User user, Sort sort);

  public int countByUser(User user);

  public boolean existsByUser(User user);

  @Query(
    "SELECT COUNT(e), e.jobDate FROM JobPost e WHERE e.user = :user GROUP BY e.jobDate ORDER BY e.jobDate DESC"
  )
  public List<Object[]> countUsersPostPerDay(@Param("user") User user);

  @Query(
    "SELECT e.user, COUNT(e) FROM JobPost e WHERE e.jobDate=:date GROUP BY e.user ORDER BY COUNT(e) DESC"
  )
  public List<Object[]> topPerformersOfTheDay(@Param("date") LocalDate date);

  // @Query(
  //   "SELECT jp FROM JobPost jp WHERE " +
  //   "jp.jobTitle LIKE %:jobTitle% OR " +
  //   "jp.companyName LIKE %:companyName% OR " +
  //   "jp.jobDescription LIKE %:jobDescription% OR " +
  //   "jp.jobDate = :jobDate OR " +
  //   "jp.status = :jobStatus ORDER BY jp.jobDate DESC"
  // )
  @Query(
    "SELECT jp FROM JobPost jp WHERE " +
    "(jp.jobTitle LIKE %:jobTitle% OR " +
    "jp.companyName LIKE %:companyName% OR " +
    "jp.jobDescription LIKE %:jobDescription%) AND " +
    "(jp.jobDate = :jobDate AND jp.status = :jobStatus) " +
    "ORDER BY jp.jobDate DESC"
  )
  List<JobPost> findJobPostByFilters(
    @Param("jobTitle") String jobTitle,
    @Param("companyName") String companyName,
    @Param("jobDescription") String jobDescription,
    @Param("jobDate") LocalDate jobDate,
    @Param("jobStatus") JobStatusEnum jobStatus
  );
}
