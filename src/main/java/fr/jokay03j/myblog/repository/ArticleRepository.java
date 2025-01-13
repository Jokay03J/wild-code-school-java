package fr.jokay03j.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jokay03j.myblog.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
