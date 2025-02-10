package fr.jokay03j.myblog.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fr.jokay03j.myblog.dto.ArticleDTO;
import fr.jokay03j.myblog.dto.AuthorDTO;
import fr.jokay03j.myblog.dto.CategoryDTO;
import fr.jokay03j.myblog.model.Article;
import fr.jokay03j.myblog.model.Image;

@Component
public class ArticleMapper {

  static public ArticleDTO convert(Article article) {
    ArticleDTO dto = new ArticleDTO();
    dto.setId(article.getId());
    dto.setTitle(article.getTitle());
    dto.setContent(article.getContent());
    dto.setCreatedAt(article.getCreatedAt());
    dto.setUpdatedAt(article.getUpdatedAt());
    if (article.getCategory() != null) {
      CategoryDTO category = new CategoryDTO();
      category.setId(article.getCategory().getId());
      category.setName(article.getCategory().getName());
      category.setCreatedAt(article.getCategory().getCreatedAt());
      category.setUpdatedAt(article.getCategory().getUpdatedAt());
      dto.setCategory(category);
    }

    if (article.getImages() != null) {
      dto.setImageUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
    }

    if (article.getArticleAuthors() != null) {
      dto.setAuthors(article.getArticleAuthors().stream()
          .filter(articleAuthor -> articleAuthor.getAuthor() != null)
          .map(articleAuthor -> {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setId(articleAuthor.getAuthor().getId());
            authorDTO.setFirstname(articleAuthor.getAuthor().getFirstname());
            authorDTO.setLastname(articleAuthor.getAuthor().getLastname());
            return authorDTO;
          })
          .collect(Collectors.toList()));
    }

    return dto;
  }
}