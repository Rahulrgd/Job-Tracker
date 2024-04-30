package com.rahul.job_tracker.Config;

import com.rahul.job_tracker.JwtAuthentication.JwtAuthenticationFilter;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@AllArgsConstructor
public class SecurityFilterConfig {

  @Autowired
  private AuthenticationEntryPoint point;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private String allowedOrigin;

  public SecurityFilterConfig() {}

  public SecurityFilterConfig(
    @Value("${allowed.origin}") String allowedOrigin
  ) {
    this.allowedOrigin = allowedOrigin;
  }

  public String getOrigin() {
    return allowedOrigin;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowedOrigins(Arrays.asList(getOrigin()));
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    // configuration.setAllowedOrigins(Arrays.asList("http://job-tracker-and-manager.s3-website.eu-north-1.amazonaws.com"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    throws Exception {
    httpSecurity
      .csrf(csrf -> csrf.disable())
      .cors(cors -> corsConfigurationSource())
      .headers(header ->
        header.frameOptions(frameOptions -> frameOptions.disable())
      )
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers("/", "/sign-up/", "/authenticate", "/v1/dashboard/**")
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
