package com.rahul.job_tracker.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.job_tracker.JobPost.JobPost;
import com.rahul.job_tracker.Resume.Resume;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String fullName;

  @Column(unique = true)
  @Email(message = "Please provide a valid email")
  private String email;

  private String password;
  
  @Nullable
  @JsonIgnore
  private String role;

  public UserDTO toDTO(){
    UserDTO dto = new UserDTO();
    // dto.setId(this.id); //Not Needed
    dto.setEmail(this.email);
    dto.setFullName(this.fullName);
    dto.setRole(this.role);
    if(this.resumes != null){
      dto.setResume(this.resumes.stream().map(Resume::getResumeName).collect(Collectors.toList()));
    }
    return dto;
  }

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Nullable
  @JsonIgnore
  private List<JobPost> jobPosts = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Nullable
  @JsonIgnore
  private List<Resume> resumes = new ArrayList<>();

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO Auto-generated method stub
    return Collections.singletonList(new SimpleGrantedAuthority(role));
  }

  @Override
  public String getUsername() {
    // TODO Auto-generated method stub
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
