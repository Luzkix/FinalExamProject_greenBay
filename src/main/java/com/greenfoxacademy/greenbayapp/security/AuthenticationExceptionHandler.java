package com.greenfoxacademy.greenbayapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.dtos.ErrorDTO;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {
    ObjectMapper mapper = new ObjectMapper();
    String responseDTO = mapper.writeValueAsString(new ErrorDTO("Unauthorised request!"));

    response.getWriter().write(responseDTO);
    response.addHeader("Content-Type", "application/json");
    response.setStatus(SecurityConfig.FAILURE_STATUSCODE);
  }
}
