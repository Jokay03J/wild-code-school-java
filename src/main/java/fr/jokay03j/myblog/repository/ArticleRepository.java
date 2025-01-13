package fr.jokay03j.myblog.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jokay03j.myblog.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);

    List<Article> findByContentContaining(String content);

    List<Article> findByCreatedAtAfter(LocalDateTime createdAt);

    List<Article> findByTitleOrderByCreatedAtDesc(String title);
}
