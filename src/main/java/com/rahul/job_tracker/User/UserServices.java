package com.rahul.job_tracker.User;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface UserServices {

  public User getAuthenticatedUser();

  public UserDTO createUser(User user);

  public List<UserDTO> getAllUsers(int pageNumber);

  public UserDTO getUserDetails();

  public Long countTotalUsers();
}
