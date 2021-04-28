package com.greenfoxacademy.greenbayapp.user.services;

import com.greenfoxacademy.greenbayapp.factories.UserFactory;
import com.greenfoxacademy.greenbayapp.security.jwt.JwtProvider;
import com.greenfoxacademy.greenbayapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.RegisterResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.UserTokenDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {
  private UserServiceImpl userService;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JwtProvider jwtProvider;

  @Before
  public void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    jwtProvider = Mockito.mock(JwtProvider.class);
    userService = Mockito.spy(new UserServiceImpl(userRepository,passwordEncoder,jwtProvider));
  }

  @Test
  public void registerNewUserReturnsCorrectUser() {
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();
    UserEntity savedUser = UserFactory.createDefaultUser(0L);
    Mockito.when(userRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(dto.getUsername(),dto.getEmail()))
        .thenReturn(false);
    Mockito.doReturn(savedUser).when(userService).saveNewUser(dto);

    UserEntity result = userService.registerNewUser(dto);

    Assert.assertEquals("zdenek", result.getUsername());
    Assert.assertEquals("password", result.getPassword());
    Assert.assertEquals(0l, result.getDollars().longValue());
  }

  @Test(expected = RuntimeException.class)
  public void registerUserThrowsNewRuntimeException() {
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();
    Mockito.when(userRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(dto.getUsername(), dto.getEmail()))
        .thenThrow(RuntimeException.class);

    UserEntity result = userService.registerNewUser(dto);
  }

  @Test
  public void saveNewUserReturnsCorrectUser() {
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();
    UserEntity newUser = UserFactory.createUser("zdenek","test@seznam.cz","password");
    Mockito.when(passwordEncoder.encode(dto.getPassword())).thenReturn("password");
    Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

    UserEntity savedUser = UserFactory.createDefaultUser(0l);

    Assert.assertEquals("zdenek", savedUser.getUsername());
    Assert.assertEquals("test@seznam.cz", savedUser.getEmail());
    Assert.assertEquals("password", savedUser.getPassword());
  }

  @Test
  public void convertUserToRegisterResponseDTO_returnsCorrectResponseDTO() {
   UserEntity user = UserFactory.createDefaultUser(5l);
   user.setId(1l);

    RegisterResponseDTO savedUser = userService.convertUserToRegisterResponseDTO(user);
    Assert.assertEquals(1l, savedUser.getId().longValue());
    Assert.assertEquals("zdenek", savedUser.getUsername());
    Assert.assertEquals("test@seznam.cz", savedUser.getEmail());
  }

  @Test
  public void loginPlayer_returnsToken() {
    LoginRequestDTO request = UserFactory.createDefaultLoginDTO();
    UserEntity loggedUser = UserFactory.createDefaultUser(0l);
    Mockito.doReturn(loggedUser).when(userService).findUserByNameAndPassword(request.getUsername(),
        request.getPassword());
    Mockito.when(jwtProvider.generateToken(loggedUser)).thenReturn("createdToken");

    UserTokenDTO result = userService.loginPlayer(request);

    Assert.assertEquals("ok", result.getStatus());
    Assert.assertEquals("createdToken", result.getToken());
  }

  @Test(expected = RuntimeException.class)
  public void loginPlayer_throwsRuntimeException() {
    LoginRequestDTO request = UserFactory.createDefaultLoginDTO();
    Mockito.doReturn(null).when(userService).findUserByNameAndPassword(request.getUsername(),
        request.getPassword());

    UserTokenDTO result = userService.loginPlayer(request);
  }
}
