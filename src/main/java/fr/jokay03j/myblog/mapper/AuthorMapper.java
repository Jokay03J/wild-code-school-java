package fr.jokay03j.myblog.mapper;

import java.util.stream.Collectors;

import fr.jokay03j.myblog.dto.AuthorDTO;
import fr.jokay03j.myblog.dto.Author.CreateAuthorDTO;
import fr.jokay03j.myblog.model.Author;

public class AuthorMapper {
  static public AuthorDTO convert(Author author) {
    AuthorDTO authorDTO = new AuthorDTO();
    authorDTO.setId(author.getId());
    authorDTO.setFirstname(author.getFirstname());
    authorDTO.setLastname(author.getLastname());
    authorDTO.setArticles(
        author.getArticleAuthors().stream().map(article -> ArticleMapper.convert(article.getArticle()))
            .collect(Collectors.toList()));
    return authorDTO;
  }

  static public Author toEntity(CreateAuthorDTO authorDTO) {
    Author author = new Author();
    author.setFirstname(authorDTO.getFirstname());
    author.setLastname(authorDTO.getLastname());
    return author;
  }
}
