package com.greenfoxacademy.greenbayapp.security;

import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private UserService userService;

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userService.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Such user does not exist in database!");
    }
    return CustomUserDetails.provideCustomUserDetails(user);
  }
}
