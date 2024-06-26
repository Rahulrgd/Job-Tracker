package com.rahul.job_tracker.JobPost;

import com.rahul.job_tracker.User.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, UUID> {
  String sortBy = "jobDate";

  public Page<JobPost> findByUser(User user, Pageable pageable);

  public int countByUser(User user);

  public boolean existsByUser(User user);

  @Query(
    "SELECT COUNT(e), e.jobDate FROM JobPost e WHERE e.user = :user GROUP BY e.jobDate ORDER BY e.jobDate DESC"
  )
  public Page<Object[]> countUsersPostPerDay(@Param("user") User user, Pageable pageable);

  @Query(
    "SELECT e.user, COUNT(e) FROM JobPost e WHERE e.jobDate=:date GROUP BY e.user ORDER BY COUNT(e) DESC"
  )
  public List<Object[]> topPerformersOfTheDay(@Param("date") LocalDate date);

  // =============================Retrive Job Posts With Filters===================================
  @Query(
    "SELECT jp FROM JobPost jp WHERE " +
    "(jp.jobTitle LIKE %:jobTitle% OR " +
    "jp.companyName LIKE %:companyName% OR " +
    "jp.jobDescription LIKE %:jobDescription%) AND " +
    "(jp.jobDate = :jobDate AND jp.status = :jobStatus) " +
    "ORDER BY jp.jobDate DESC"
  )
  public List<JobPost> findJobPostByFilters(
    @Param("jobTitle") String jobTitle,
    @Param("companyName") String companyName,
    @Param("jobDescription") String jobDescription,
    @Param("jobDate") LocalDate jobDate,
    @Param("jobStatus") JobStatusEnum jobStatus
  );

  // ============================Retrive Job Posts Containg String======================================
  @Query(
    "SELECT jp FROM JobPost jp " +
    "LEFT JOIN jp.user u " +
    "WHERE jp.jobTitle LIKE %:string% OR " +
    "jp.companyName LIKE %:string% OR " +
    "jp.jobDescription LIKE %:string% OR " +
    "jp.status LIKE %:string% OR " +
    "u.fullName LIKE %:string% " +
    "ORDER BY jp.jobDate "
  )
  public Page<JobPost> findJobPostContaingString(
    @Param("string") String string,
    Pageable pageable
  );

  // ============================Retrive Users Job Posts Containg String======================================
  @Query(
    "Select jp FROM JobPost jp WHERE " +
    "(jp.user =:user) AND " +
    "(jp.jobTitle LIKE %:string% OR " +
    "jp.companyName LIKE %:string% OR " +
    "jp.status LIKE %:string% OR " +
    "jp.jobDescription LIKE %:string%)"
  )
  Page<JobPost> findUserJobPostContaingString(
    User user,
    @Param("string") String string,
    Pageable pageable
  );

  // ===============================Retrive Job Posts Per Day====================================
  @Query(
    "SELECT count(jp), jp.jobDate FROM JobPost jp WHERE jp.clone = false GROUP BY jp.jobDate ORDER BY jp.jobDate DESC"
  )
  public Page<Object[]> findJobCountPerDay(Pageable pageable);
}
