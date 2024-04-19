package com.rahul.job_tracker.UserClasses;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserServices userServices;

  @GetMapping("/all-users")
  public List<User> getAllUsers() {
    return userServices.getAllUsers();
  }

  @PostMapping("/create-user")
  public ResponseEntity<User> createUser(@RequestBody User user){
    return userServices.createUser(user);
  }
}
