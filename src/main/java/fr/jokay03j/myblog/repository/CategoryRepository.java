package fr.jokay03j.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jokay03j.myblog.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
