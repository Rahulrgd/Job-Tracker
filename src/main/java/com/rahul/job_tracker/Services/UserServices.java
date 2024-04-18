package com.rahul.job_tracker.Services;

import com.rahul.job_tracker.Entities.User;
import com.rahul.job_tracker.Repositories.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

  @Autowired
  private UserRepository userRepository;

  public ResponseEntity<User> createUser(User user) {
    userRepository.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  public List<User> getAllUsers(){
    return userRepository.findAll();
  }
}
