package fr.jokay03j.myblog.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.jokay03j.myblog.exception.BadRequestException;
import fr.jokay03j.myblog.model.User;
import fr.jokay03j.myblog.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User registerUser(String email, String password, Set<String> roles) {
    if (userRepository.existsByEmail(email)) {
      throw new BadRequestException("Cet email est déjà utilisé");
    }

    User user = new User();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password)); // Encodage du mot de passe avec BCrypt
    user.setRoles(roles);
    User savedUser = userRepository.save(user);
    savedUser.setPassword(null); // On ne renvoie pas le mot de passe
    return savedUser;
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }
}