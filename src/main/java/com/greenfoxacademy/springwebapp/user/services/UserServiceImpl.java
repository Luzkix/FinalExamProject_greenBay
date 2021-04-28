package com.greenfoxacademy.springwebapp.user.services;

import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import com.greenfoxacademy.springwebapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.UserTokenDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.user.models.UserEntity;
import com.greenfoxacademy.springwebapp.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JwtProvider jwtProvider;

  @Override
  public UserEntity registerNewUser(RegisterRequestDTO registerRequestDTO)  throws RuntimeException{
    if(userRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(
        registerRequestDTO.getUsername(),
        registerRequestDTO.getEmail())) {
      throw new RuntimeException("Username or email is already taken!");
    }

    UserEntity savedUser = saveNewUser(registerRequestDTO);
    return savedUser;
  }

  public UserEntity saveNewUser(RegisterRequestDTO dto) {
    UserEntity newUser = new UserEntity(
        dto.getUsername(),
        dto.getEmail(),
        passwordEncoder.encode(dto.getPassword()));

    return userRepository.save(newUser);
  }

  @Override
  public RegisterResponseDTO convertUserToRegisterResponseDTO(UserEntity newUser) {
    RegisterResponseDTO responseDTO = new RegisterResponseDTO();
    BeanUtils.copyProperties(newUser, responseDTO);
    return responseDTO;
  }

  @Override
  public UserTokenDTO loginPlayer(LoginRequestDTO request) throws RuntimeException {
    UserEntity loggedUser = findUserByNameAndPassword(request.getUsername(), request.getPassword());
    if(loggedUser == null) throw new RuntimeException("Username or password is incorrect!");
    String token = jwtProvider.generateToken(loggedUser);
    return new UserTokenDTO(token);
  }

  public UserEntity findUserByNameAndPassword(String username, String password) {
    UserEntity user = userRepository.findByUsername(username);
    if(user != null) {
      if(passwordEncoder.matches(password, user.getPassword())) return user;
    }
    return null;
  }
}
