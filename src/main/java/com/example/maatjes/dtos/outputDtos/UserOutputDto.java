package com.example.maatjes.dtos.outputDtos;

import com.example.maatjes.models.Authority;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDto {
    public String username;
    public String email;
    //todo onderstaande wil ik niet tonen aan iedereen.
    @JsonSerialize
    public Set<Authority> authorities;
}