package com.greenfoxacademy.springwebapp.user.repositories;

import com.greenfoxacademy.springwebapp.user.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByUsernameOrEmail(String username, String email);

  UserEntity findByUsername(String username);
}
