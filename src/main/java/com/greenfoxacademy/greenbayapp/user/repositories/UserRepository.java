package com.greenfoxacademy.greenbayapp.user.repositories;

import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

  UserEntity findByUsername(String username);
}
