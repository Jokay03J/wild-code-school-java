package fr.jokay03j.myblog.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jokay03j.myblog.dto.User.UserRegistrationDTO;
import fr.jokay03j.myblog.model.User;
import fr.jokay03j.myblog.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
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
}