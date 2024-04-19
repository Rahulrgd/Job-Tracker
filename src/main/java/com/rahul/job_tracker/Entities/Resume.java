package com.rahul.job_tracker.Entities;

import java.util.Optional;

import com.rahul.job_tracker.UserClasses.User;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resume {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @Nullable
  private User user;

  @Lob
  private byte[] resumeFile;

  public void setResume(byte[] resume) {
    int resumeSize = 200 * 1024;
    if (resume != null && resume.length <= resumeSize) {
      this.resumeFile = resume;
    } else {
      throw new IllegalArgumentException(
        "File size exceeds the maximum allowed size of 200 KB"
      );
    }
  }
}
