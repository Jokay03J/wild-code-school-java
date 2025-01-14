package fr.jokay03j.myblog.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jokay03j.myblog.model.Article;
import fr.jokay03j.myblog.model.Category;
import fr.jokay03j.myblog.repository.ArticleRepository;
import fr.jokay03j.myblog.repository.CategoryRepository;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository repository;
    private final CategoryRepository categoryRepository;

    public ArticleController(ArticleRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = this.repository.findAll();

        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search-title")
    public ResponseEntity<List<Article>> searchTitle(@RequestParam String search) {
        List<Article> articles = this.repository.findByTitle(search);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search-content")
    public ResponseEntity<List<Article>> getArticlesByContent(@RequestParam String content) {
        List<Article> articles = this.repository.findByContentContaining(content);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search-after")
    public ResponseEntity<List<Article>> getArticlesCreateAfter(@RequestParam LocalDateTime time) {
        List<Article> articles = this.repository.findByCreatedAtAfter(time);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/last")
    public ResponseEntity<List<Article>> getFiveLastArticles() {
        List<Article> articles = this.repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles.stream().limit(5).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Article article = this.repository.findById(id).orElse(null);

        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        if (article.getCategory() != null) {
            Category category = this.categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if (category == null) {
                return ResponseEntity.badRequest().build();
            }
            article.setCategory(category);
        }

        Article savedArticle = this.repository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleBody) {
        Article article = this.repository.findById(id).orElse(null);

        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        article.setContent(articleBody.getContent());
        article.setTitle(articleBody.getTitle());
        article.setUpdatedAt(LocalDateTime.now());

        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if (category == null) {
                return ResponseEntity.badRequest().body(null);
            }
            article.setCategory(category);
        }

        Article updatedArticle = this.repository.save(article);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteArticle(@PathVariable Long id) {
        Article article = this.repository.findById(id).orElse(null);

        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        this.repository.delete(article);
        return ResponseEntity.noContent().build();
    }
}
