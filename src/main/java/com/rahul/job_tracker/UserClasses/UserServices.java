package com.rahul.job_tracker.UserClasses;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public ResponseEntity<User> createUser(User user) {
    userRepository.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
      User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
      return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
      );
  }

  public User saveUser(User user){
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }
}
