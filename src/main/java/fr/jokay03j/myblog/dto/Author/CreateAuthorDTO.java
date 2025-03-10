package fr.jokay03j.myblog.dto.Author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAuthorDTO {

  @NotBlank(message = "Le prénom de l'auteur ne doit pas être vide")
  @Size(min = 1, message = "Le prénom de l'auteur doit contenir au moins 1 caractère")
  private String firstname;

  @NotBlank(message = "Le nom de l'auteur ne doit pas être vide")
  @Size(min = 1, message = "Le nom de l'auteur doit contenir au moins 1 caractère")
  private String lastname;

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
}
