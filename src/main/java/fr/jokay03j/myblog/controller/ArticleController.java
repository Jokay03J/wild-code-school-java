package fr.jokay03j.myblog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import fr.jokay03j.myblog.dto.ArticleDTO;
import fr.jokay03j.myblog.dto.AuthorDTO;
import fr.jokay03j.myblog.model.Article;
import fr.jokay03j.myblog.model.ArticleAuthor;
import fr.jokay03j.myblog.model.Author;
import fr.jokay03j.myblog.model.Category;
import fr.jokay03j.myblog.model.Image;
import fr.jokay03j.myblog.repository.ArticleAuthorRepository;
import fr.jokay03j.myblog.repository.ArticleRepository;
import fr.jokay03j.myblog.repository.AuthorRepository;
import fr.jokay03j.myblog.repository.CategoryRepository;
import fr.jokay03j.myblog.repository.ImageRepository;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository repository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final AuthorRepository authorRepository;

    public ArticleController(ArticleRepository repository, CategoryRepository categoryRepository,
            ImageRepository imageRepository, ArticleAuthorRepository articleAuthorRepository,
            AuthorRepository authorRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping()
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<Article> articles = this.repository.findAll();

        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles.stream().map(ArticleDTO::convert).collect(Collectors.toList()));
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
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        Article article = this.repository.findById(id).orElse(null);

        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ArticleDTO.convert(article));
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        if (article.getCategory() != null) {
            Category category = this.categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if (category == null) {
                return ResponseEntity.badRequest().build();
            }
            article.setCategory(category);
        }

        if (article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return ResponseEntity.badRequest().body(null);
                    }
                } else {
                    validImages.add(image);
                }
            }
            this.imageRepository.saveAll(validImages);
            article.setImages(validImages);
        }

        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                Author author = articleAuthor.getAuthor();
                author = this.authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return ResponseEntity.badRequest().body(null);
                }

                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(article);
                articleAuthor.setContribution(articleAuthor.getContribution());

            }
            this.articleAuthorRepository.saveAll(article.getArticleAuthors());
        }
        Article savedArticle = this.repository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(ArticleDTO.convert(savedArticle));
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleBody) {
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

        if (article.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }

        if (article.getArticleAuthors() != null) {
            // Supprimer manuellement les anciens ArticleAuthor
            for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : article.getArticleAuthors()) {
                Author author = articleAuthorDetails.getAuthor();
                author = authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return ResponseEntity.badRequest().build();
                }

                // Créer et associer la nouvelle relation ArticleAuthor
                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);
            }

            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                articleAuthorRepository.save(articleAuthor);
            }

            article.setArticleAuthors(updatedArticleAuthors);
        }

        Article updatedArticle = this.repository.save(article);
        return ResponseEntity.ok(ArticleDTO.convert(updatedArticle));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteArticle(@PathVariable Long id) {
        Article article = this.repository.findById(id).orElse(null);

        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                articleAuthorRepository.delete(articleAuthor);
            }
        }

        this.repository.delete(article);
        return ResponseEntity.noContent().build();
    }
}
