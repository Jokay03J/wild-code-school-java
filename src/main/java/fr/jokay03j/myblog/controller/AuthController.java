package fr.jokay03j.myblog.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jokay03j.myblog.dto.User.UserLoginDTO;
import fr.jokay03j.myblog.dto.User.UserRegistrationDTO;
import fr.jokay03j.myblog.model.User;
import fr.jokay03j.myblog.service.AuthenticationService;
import fr.jokay03j.myblog.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;
  private final AuthenticationService authenticationService;

  public AuthController(UserService userService, AuthenticationService authenticationService) {
    this.userService = userService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
    User registeredUser = userService.registerUser(
        userRegistrationDTO.getEmail(),
        userRegistrationDTO.getPassword(),
        Set.of("ROLE_USER") // Par défaut, chaque utilisateur aura le rôle "USER"
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
  }

  @PostMapping("/login")
  public ResponseEntity<String> authenticate(@Valid @RequestBody UserLoginDTO userLoginDTO) {
    String token = authenticationService.authenticate(
        userLoginDTO.getEmail(),
        userLoginDTO.getPassword());
    return ResponseEntity.ok(token);
  }
}