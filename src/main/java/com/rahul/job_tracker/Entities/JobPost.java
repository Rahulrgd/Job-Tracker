package com.rahul.job_tracker.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.job_tracker.DTO.JobPostDTO;
import com.rahul.job_tracker.UserClasses.User;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPost {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String jobTitle;
  private String companyName;
  private LocalDate jobDate;

  @Column(length = 512)
  private String jobDescription;

  @Column(length = 512)
  private String jobLink;

  @Enumerated(EnumType.STRING)
  private JobStatusEnum Status;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @Nullable
  @JsonIgnore
  private User user;

  @OneToOne
  @PrimaryKeyJoinColumn
  @Nullable
  @JsonIgnore
  private Resume resume;

  public JobPostDTO toDTO() {
    JobPostDTO dto = new JobPostDTO();
    dto.setJobPostId(this.id);
    dto.setJobTitle(this.jobTitle);
    dto.setCompanyName(this.companyName);
    dto.setJobDescription(this.jobDescription);
    dto.setJobDate(this.jobDate);
    dto.setJobLink(this.jobLink);
    dto.setUsername(user.getUsername());
    return dto;
  }

  @Nullable
  private String resumeName;

  public void setResumeName() {
    this.resumeName = resume.getResumeName();
  }
}
