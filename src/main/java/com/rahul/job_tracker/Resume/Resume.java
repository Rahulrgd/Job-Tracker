package com.rahul.job_tracker.Resume;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.job_tracker.Entities.User;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;
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
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private User user;

  private String resumeName;

  public void setResumeName(String resumeName) {
    this.resumeName = resumeName;
  }

  public String extractFileName(byte[] resume) {
    try {
      // Create a temporary file from the byte array
      java.nio.file.Path tempFile = Files.createTempFile("temp-resume", ".tmp");
      Files.write(tempFile, resume);

      // Get the file name from the temporary file path
      String fileName = tempFile.getFileName().toString();

      // Assign the file name to resumeName
      resumeName = fileName;

      // Delete the temporary file
      Files.delete(tempFile);

      return fileName;
    } catch (Exception e) {
      throw new RuntimeException("Failed to extract file name from resume", e);
    }
  }

  @Lob
  @Column(name = "resume_file", columnDefinition = "BLOB")
  private byte[] resumeFile;

  public void setResume(byte[] resume) {
    int resumeSize = 200 * 1024; // 200 KB
    if (resume != null && resume.length <= resumeSize) {
      this.resumeFile = resume;
    } else {
      throw new IllegalArgumentException(
        "File size exceeds the maximum allowed size of 200 KB"
      );
    }
  }

  public ResumeDTO toDTO(){
    ResumeDTO dto = new ResumeDTO();
    dto.setResumeId(this.id);
    dto.setResumeName(getResumeName());
    dto.setUser(this.user);
    return dto;
  }
}
