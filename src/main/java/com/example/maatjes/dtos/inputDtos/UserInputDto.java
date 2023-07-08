package com.example.maatjes.dtos.inputDtos;

import com.example.maatjes.models.Authority;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDto {
    @NotBlank
    public String username;
    @NotBlank (message = "Je moet je wachtwoord invullen")
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[\\!\\#\\@\\$\\%\\&\\/\\(\\)\\=\\?\\*\\-\\+\\_\\.\\:\\;\\,\\{\\}\\^])[A-Za-z0-9!#@$%&/()=?*+-_.:;,{}]{8,20}", message = "Je wachtwoord moet bestaan uit: Eén kleine letter, één grote letter, één getal, één symbool en tussen de 8 tot 20 karakters.")
    public String password;
    @Email(message = "Ongeldig emailadres") @NotBlank
    public String email;
    @JsonSerialize
    public Set<Authority> authorities;
}
