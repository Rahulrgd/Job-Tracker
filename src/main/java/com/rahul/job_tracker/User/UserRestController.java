package com.rahul.job_tracker.User;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

  @Autowired
  private UserServices userServices;

  @GetMapping("/all-users")
  public List<User> getAllUsers() {
    return userServices.getAllUsers();
  }

  @PostMapping("/sign-up/")
  public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
    return userServices.createUser(user);
  }

  @GetMapping("/v1/user-detail")
  public UserDTO getUserDetails() {
    return userServices.getUserDetails();
  }

  @GetMapping("/v1/dashboard/count-total-users")
  public ResponseEntity<Long> countTotalUsers() {
    return userServices.countTotalUsers();
  }
}
