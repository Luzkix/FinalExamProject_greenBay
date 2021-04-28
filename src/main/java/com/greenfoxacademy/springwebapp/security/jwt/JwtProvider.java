package com.greenfoxacademy.springwebapp.security.jwt;

import com.greenfoxacademy.springwebapp.user.models.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

  //@Value("${JWT.SECRET}")
  private String jwtSecret = "topSecret";
  //@Value("${JWT_EXPIRE_DAYS:1}")
  private int jwtExpireDays = 3650;

  public String generateToken(UserEntity user) {
    Date expirationDate = Date.from(LocalDate.now().plusDays(jwtExpireDays).atStartOfDay(ZoneId.systemDefault()).toInstant());

    HashMap<String, Object> claims = new HashMap<>();
    claims.put("username",user.getUsername());

    String token = Jwts.builder()
        .setClaims(claims)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

    return token;
  }
}
