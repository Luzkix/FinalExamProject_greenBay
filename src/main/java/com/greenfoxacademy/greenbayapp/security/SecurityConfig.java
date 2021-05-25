package com.greenfoxacademy.greenbayapp.security;

import com.greenfoxacademy.greenbayapp.security.jwt.JwtFilter;
import com.greenfoxacademy.greenbayapp.user.controllers.UserController;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  public static final int FAILURE_STATUSCODE = 401;

  private AuthenticationExceptionHandler authenticationExceptionHandler;
  private JwtFilter jwtFilter;

  public SecurityConfig(AuthenticationExceptionHandler authenticationExceptionHandler, JwtFilter jwtFilter) {
    this.authenticationExceptionHandler = authenticationExceptionHandler;
    this.jwtFilter = jwtFilter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(UserController.REGISTER, UserController.LOGIN).permitAll()
        .anyRequest().authenticated() //all requests except endpoints above requires authentication
        .and()
        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(authenticationExceptionHandler)
    ;
  }
}
