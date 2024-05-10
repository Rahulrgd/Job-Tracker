package com.rahul.job_tracker.User;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    // private UUID id; //Not Needed
    private String fullName;
    private String email;
    private String role;
    private List<ResumeDTO> resumeDTO;
}
