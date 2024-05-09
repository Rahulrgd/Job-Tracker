package com.rahul.job_tracker.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rahul.job_tracker.DTO.UserDTO;
import com.rahul.job_tracker.Entities.User;
import com.rahul.job_tracker.Repositories.UserRepository;

@Service
public class UserServices {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User getAuthenticatedUser() {
    User user = (User) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
    return user;
  }

  public ResponseEntity<UserDTO> createUser(User user) {
    User newUser = new User();
    newUser.setFullName(user.getFullName());
    newUser.setEmail(user.getEmail());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setRole("user");
    userRepository.save(newUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(newUser.toDTO());
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public UserDTO getUserDetails() {
    Optional<User> optionalUser = userRepository.findById(
      getAuthenticatedUser().getId()
    );
    User user = optionalUser.orElseThrow(() ->
      new IllegalArgumentException("User Not Found with ID: " + getAuthenticatedUser().getId())
    );
    return user.toDTO();
  }

  // =========================Count Total Users=============================
  public ResponseEntity<Long> countTotalUsers(){
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(userRepository.count());
  }
}
