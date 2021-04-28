package com.greenfoxacademy.greenbayapp.security.jwt;

import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetailsService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtProvider jwtProvider;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    //1.checking if request is provided with bearer token
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }
    //2. if so, extracting token from request and checking its validity
    String token = getTokenFromRequest(request);
    Boolean tokenIsValid = validateToken(token, request, response);

    //3. if it is valid, setting security context with all required info.
    if (token != null && tokenIsValid) {
      authenticateUser(token);
    }
    filterChain.doFilter(request, response);
  }

  private void authenticateUser(String token) {
    String username = jwtProvider.getLoginFromToken(token);
    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        customUserDetails, //this contains everything: user, username, psw, authorities(if any)
        null,
        customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    log.info("Authenticated user: {}", customUserDetails.getUsername());
  }

  private Boolean validateToken(String token, HttpServletRequest request, HttpServletResponse response) {
    Boolean tokenIsValid = false;
    try {
      tokenIsValid = jwtProvider.validateToken(token);
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      log.error("Token validation error!");
    }
    return tokenIsValid;
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
