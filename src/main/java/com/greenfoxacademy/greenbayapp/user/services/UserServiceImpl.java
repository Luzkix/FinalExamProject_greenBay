package com.greenfoxacademy.greenbayapp.user.services;

import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.security.jwt.JwtProvider;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.UserTokenDTO;
import com.greenfoxacademy.greenbayapp.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JwtProvider jwtProvider;

  @Override
  public UserEntity registerNewUser(RegisterRequestDTO registerRequestDTO)  throws
      AuthorizationException {
    if (userRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(
        registerRequestDTO.getUsername(),
        registerRequestDTO.getEmail())) {
      throw new AuthorizationException("Username or email is already taken!");
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
  public UserTokenDTO loginPlayer(LoginRequestDTO request) throws AuthorizationException {
    UserEntity loggedUser = findUserByNameAndPassword(request.getUsername(), request.getPassword());
    if (loggedUser == null) throw new AuthorizationException("Username or password is incorrect!");
    String token = jwtProvider.generateToken(loggedUser);
    return new UserTokenDTO(token);
  }

  public UserEntity findUserByNameAndPassword(String username, String password) {
    UserEntity user = userRepository.findByUsername(username);
    if (user != null) {
      if (passwordEncoder.matches(password, user.getPassword())) return user;
    }
    return null;
  }

  @Override
  public UserEntity findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
