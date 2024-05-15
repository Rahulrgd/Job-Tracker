package com.rahul.job_tracker.JwtAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Slf4j
@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtHelper jwtHelper;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    // Bearer
    String requestHeader = request.getHeader("Authorization");
    log.info("Header :  {}", requestHeader);
    String email = null;
    String token = null;

    if (requestHeader != null && requestHeader.startsWith("Bearer")) {
        //looking good
        token = requestHeader.substring(7);
        try {
            email = this.jwtHelper.getEmailFromToken(token);
        } catch (IllegalArgumentException e) {
            logger.info("Illegal Argument while fetching the email !!");
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            logger.info("Given jwt token is expired !!");
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            logger.info("Some changed has done in token !! Invalid Token");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        logger.info("Invalid Header Value !! ");
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        //fetch user detail from email
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
        if (validateToken) {
            //set the authentication
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            logger.info("Validation fails !!");
        }
    }
    filterChain.doFilter(request, response);
  }
}
