package com.rahul.job_tracker.JobPost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.job_tracker.Resume.Resume;
import com.rahul.job_tracker.User.User;

import jakarta.annotation.Nullable;
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
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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

  @Nullable
  private boolean clone;

  @Size(min = 3, message = "Job title must have at least 3 characters.")
  private String jobTitle;

  @Size(min = 3, message = "Comapany name must have at least 3 characters.")
  private String companyName;

  @Nullable
  private LocalDate jobDate;

  // @Column(length = 2560)
  @Column(length = 10000)
  @Size(min = 3, message = "Job Description must have at least 3 characters.")
  private String jobDescription;

  @Column(length = 2048)
  private String jobLink;

  @Enumerated(EnumType.STRING)
  private JobStatusEnum status;

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

  @Nullable
  private String resumeName;

  public void setResumeName() {
    this.resumeName = resume.getResumeName();
  }

  // public JobPostDTO toDTO() {
  //   JobPostDTO dto = new JobPostDTO();
  //   dto.setJobPostId(this.id);
  //   dto.setJobTitle(this.jobTitle);
  //   dto.setCompanyName(this.companyName);
  //   dto.setJobDescription(this.jobDescription);
  //   dto.setJobDate(this.jobDate);
  //   dto.setJobLink(this.jobLink);
  //   dto.setUsername(user.getFullName());
  //   dto.setClone(this.clone);
  //   dto.setJobStatus(this.status);
  //   return dto;
  // }
}
