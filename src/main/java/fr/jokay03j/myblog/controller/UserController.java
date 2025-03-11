package fr.jokay03j.myblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jokay03j.myblog.model.User;
import fr.jokay03j.myblog.service.UserService;

@RestController
@RequestMapping("/profile")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
  public ResponseEntity<User> getUserProfile(@PathVariable Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!((User) auth.getPrincipal()).getId().equals(id)) {
      throw new RuntimeException("Vous n'êtes pas autorisé à accéder à ce profil");
    }
    User user = userService.findById(id)
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    return ResponseEntity.ok(user);
  }
}