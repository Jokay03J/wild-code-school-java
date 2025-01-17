package fr.jokay03j.myblog.dto;

import java.util.List;

import fr.jokay03j.myblog.model.Author;

public class AuthorDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private List<Long> articleIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Long> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }

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
