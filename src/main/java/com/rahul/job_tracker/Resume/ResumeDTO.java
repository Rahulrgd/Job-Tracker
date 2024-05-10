package com.rahul.job_tracker.Resume;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.job_tracker.User.User;

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

  private UUID id;
  private String resumeName;

  @JsonIgnore
  private User user;
}
