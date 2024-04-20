package com.rahul.job_tracker.DTO;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostDTO {

  private UUID jobPostId;

  private String jobTitle;
  private String companyName;
  private String jobDescription;
  private LocalDate jobDate;
  private String jobLink;
}
