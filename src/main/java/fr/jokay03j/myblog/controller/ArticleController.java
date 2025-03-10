package fr.jokay03j.myblog.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jokay03j.myblog.dto.ArticleDTO;
import fr.jokay03j.myblog.dto.Article.CreateArticleDTO;
import fr.jokay03j.myblog.model.Article;
import fr.jokay03j.myblog.service.ArticleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = this.articleService.getArticles();

        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles);
    }

    @GetMapping("{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = this.articleService.getArticleById(id);

        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody CreateArticleDTO article) {
        ArticleDTO savedArticle = this.articleService.createArticle(article);
        if (savedArticle == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleBody) {
        ArticleDTO updatedArticle = this.articleService.updateArticle(articleBody, id);

        if (updatedArticle == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteArticle(@PathVariable Long id) {
        Long deletedArticleId = this.articleService.deleteArticle(id);
        if (deletedArticleId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(id);
    }
}
