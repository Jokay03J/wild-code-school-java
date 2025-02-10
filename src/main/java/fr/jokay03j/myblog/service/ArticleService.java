package fr.jokay03j.myblog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.jokay03j.myblog.dto.ArticleDTO;
import fr.jokay03j.myblog.exception.ResourceNotFoundException;
import fr.jokay03j.myblog.mapper.ArticleMapper;
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

@Service
public class ArticleService {
  private final ArticleRepository articleRepository;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final AuthorRepository authorRepository;
  private final ArticleAuthorRepository articleAuthorRepository;

  public ArticleService(
      ArticleRepository articleRepository,
      ArticleMapper articleMapper,
      CategoryRepository categoryRepository,
      ImageRepository imageRepository,
      AuthorRepository authorRepository,
      ArticleAuthorRepository articleAuthorRepository) {
    this.articleRepository = articleRepository;
    this.categoryRepository = categoryRepository;
    this.imageRepository = imageRepository;
    this.authorRepository = authorRepository;
    this.articleAuthorRepository = articleAuthorRepository;
  }

  public List<ArticleDTO> getArticles() {
    List<Article> articles = this.articleRepository.findAll();

    return articles.stream().map(ArticleMapper::convert).collect(Collectors.toList());
  }

  public ArticleDTO getArticleById(Long id) {
    Article article = this.articleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

    return ArticleMapper.convert(article);
  }

  public ArticleDTO createArticle(Article article) {
    article.setCreatedAt(LocalDateTime.now());
    article.setUpdatedAt(LocalDateTime.now());

    if (article.getCategory() != null) {
      Category category = this.categoryRepository.findById(article.getCategory().getId())
          .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
      article.setCategory(category);
    }

    if (article.getImages() != null && !article.getImages().isEmpty()) {
      List<Image> validImages = new ArrayList<>();
      for (Image image : article.getImages()) {
        if (image.getId() != null) {
          Image existingImage = imageRepository.findById(image.getId())
              .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
          validImages.add(existingImage);
        } else {
          validImages.add(image);
        }
      }
      this.imageRepository.saveAll(validImages);
      article.setImages(validImages);
    }
    Article savedArticle = this.articleRepository.save(article);

    if (article.getArticleAuthors() != null) {
      for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
        Author author = articleAuthor.getAuthor();
        System.out.println(author);
        if (author == null) {
          return null;
        }

        author = this.authorRepository.findById(author.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        articleAuthor.setAuthor(author);
        articleAuthor.setArticle(article);
        articleAuthor.setContribution(articleAuthor.getContribution());

      }
    }
    this.articleAuthorRepository.saveAll(article.getArticleAuthors());
    return ArticleMapper.convert(savedArticle);
  }

  public ArticleDTO updateArticle(Article articleBody, Long id) {
    Article article = this.articleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

    article.setContent(articleBody.getContent());
    article.setTitle(articleBody.getTitle());
    article.setUpdatedAt(LocalDateTime.now());

    if (article.getCategory() != null) {
      Category category = categoryRepository.findById(article.getCategory().getId())
          .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
      article.setCategory(category);
    }

    if (article.getImages() != null) {
      List<Image> validImages = new ArrayList<>();
      for (Image image : article.getImages()) {
        if (image.getId() != null) {
          Image existingImage = imageRepository.findById(image.getId())
              .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
          validImages.add(existingImage);

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
        author = authorRepository.findById(author.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

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

    Article updatedArticle = this.articleRepository.save(article);
    return ArticleMapper.convert(updatedArticle);
  }

  public Long deleteArticle(Long id) {
    Article article = this.articleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

    this.articleRepository.delete(article);
    return id;
  }
}
