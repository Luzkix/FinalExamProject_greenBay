package com.greenfoxacademy.greenbayapp.security;

import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
  private UserEntity user;
  private String username;
  private String password;
  private Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();

  public static CustomUserDetails provideCustomUserDetails(UserEntity user) {
    CustomUserDetails details = new CustomUserDetails();
    details.user = user;
    details.username = user.getUsername();
    details.password = user.getPassword();
    details.grantedAuthorities = details.getAuthorities();
    return details;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return grantedAuthorities; //returns empty authorities unless we use roles
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
