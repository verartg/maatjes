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
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[\\!\\#\\@\\$\\%\\&\\/\\(\\)\\=\\?\\*\\-\\+\\_\\.\\:\\;\\,\\{\\}\\^])[A-Za-z0-9!#@$%&/()=?*+-_.:;,{}]{8,20}", message = "Je wachtwoord moet het volgende bevatten: " +
            "1. Minimaal 1 kleine letter. 2. Minimaal 1 grote letter. 3. Minimaal 1 getal. 4. Minimaal 1 symbool. 5. Je wachtwoord moet tussen de 8 en 20 karakters bevatten.")
    public String password;
    public String email;
    @JsonSerialize
    public Set<Authority> authorities;
}
