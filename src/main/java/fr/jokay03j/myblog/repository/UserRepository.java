package fr.jokay03j.myblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jokay03j.myblog.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}