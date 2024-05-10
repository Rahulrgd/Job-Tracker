package com.rahul.job_tracker.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServicesImpl implements UserServices{

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

  public UserDTO createUser(User user) {
    User newUser = new User();
    newUser.setFullName(user.getFullName());
    newUser.setEmail(user.getEmail());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setRole("user");
    userRepository.save(newUser);
    return UserMapper.INSTANCE.toDTO(newUser);
  }

  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream().map(user->UserMapper.INSTANCE.toDTO(user)).collect(Collectors.toList());
  }

  public UserDTO getUserDetails() {
    Optional<User> optionalUser = userRepository.findById(
      getAuthenticatedUser().getId()
    );
    User user = optionalUser.orElseThrow(() ->
      new IllegalArgumentException(
        "User Not Found with ID: " + getAuthenticatedUser().getId()
      )
    );
    return UserMapper.INSTANCE.toDTO(user);
  }

  public Long countTotalUsers() {
    return userRepository.count();
  }
}
