package com.rahul.job_tracker.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  public List<UserDTO> getAllUsers(int pageNumber) {
    int pageSize = 50;
    Sort sort = Sort.by("fullName").ascending();
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
    Page<User> page=  userRepository.findAll(pageable);
    List<UserDTO> users = page.stream().map(user->UserMapper.INSTANCE.toDTO(user)).collect(Collectors.toList());
    if(page.hasContent()){
      return users;
    }else{
      if(page.hasNext()){
        pageable = pageable.next();
        page = userRepository.findAll(pageable);
      }else{
        return new ArrayList<>();
      }
    }
    return users;
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
