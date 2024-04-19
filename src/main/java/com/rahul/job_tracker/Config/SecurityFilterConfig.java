package com.rahul.job_tracker.Config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rahul.job_tracker.JwtAuthentication.JwtAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityFilterConfig {

  @Autowired
  private AuthenticationEntryPoint point;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    throws Exception {
    httpSecurity
      .csrf(csrf -> csrf.disable())
      .cors(cors->cors.disable())
      .headers(header ->
        header.frameOptions(frameOptions -> frameOptions.disable())
      )
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers("/authenticate","/h2-console/**")
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      );
    return httpSecurity.build();
  }
}
