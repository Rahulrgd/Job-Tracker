package com.rahul.job_tracker.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.job_tracker.UserClasses.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {

  private UUID resumeId;
  private String resumeName;

  @JsonIgnore
  private User user;
}
