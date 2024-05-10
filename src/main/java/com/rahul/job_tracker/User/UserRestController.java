package com.rahul.job_tracker.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

  @Autowired
  private UserServices userServices;

  @GetMapping("/all-users")
  public ResponseEntity<?> getAllUsers() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(userServices.getAllUsers());
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/sign-up/")
  public ResponseEntity<?> createUser(@RequestBody User user) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED).body(userServices.createUser(user));
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/v1/user-detail")
  public ResponseEntity<?> getUserDetails() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(userServices.getUserDetails());
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/v1/dashboard/count-total-users")
  public ResponseEntity<?> countTotalUsers() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(userServices.countTotalUsers());
      
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
