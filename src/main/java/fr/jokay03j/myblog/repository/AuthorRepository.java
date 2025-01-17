package fr.jokay03j.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jokay03j.myblog.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
