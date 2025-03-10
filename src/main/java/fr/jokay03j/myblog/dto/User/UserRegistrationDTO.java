package fr.jokay03j.myblog.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {

  @NotBlank(message = "L'email ne doit pas être vide")
  @Email(message = "L'email doit être valide")
  private String email;

  @NotBlank(message = "Le mot de passe ne doit pas être vide")
  @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
  private String password;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
