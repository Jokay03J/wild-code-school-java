package fr.jokay03j.myblog.mapper;

import fr.jokay03j.myblog.dto.AuthorDTO;
import fr.jokay03j.myblog.model.Author;

public class AuthorMapper {
  static public AuthorDTO convert(Author author) {
    AuthorDTO authorDTO = new AuthorDTO();
    authorDTO.setId(author.getId());
    authorDTO.setFirstname(author.getFirstname());
    authorDTO.setLastname(author.getLastname());
    // authorDTO.setArticleIds(
    // author.getArticleIds().stream().map(article ->
    // article.getId()).collect(Collectors.toList()));
    return authorDTO;
  }
}
