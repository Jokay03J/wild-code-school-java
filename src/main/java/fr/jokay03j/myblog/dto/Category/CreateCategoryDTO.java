package fr.jokay03j.myblog.dto.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryDTO {

  @NotBlank(message = "Le nom de la catégorie ne doit pas être vide")
  @Size(min = 1, message = "Le nom de la catégorie doit contenir au moins 1 caractère")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
