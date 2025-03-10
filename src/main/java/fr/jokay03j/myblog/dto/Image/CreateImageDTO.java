package fr.jokay03j.myblog.dto.Image;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public class CreateImageDTO {
  @NotBlank(message = "L'url de l'image ne peut pas être vide")
  @URL(message = "L'url de l'image doit être une URL valide")
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
